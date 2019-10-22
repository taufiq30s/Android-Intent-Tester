package com.mikroskil.androiddasar.intent

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_capture_akarin.*
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.nfc.Tag
import android.os.Environment
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import org.w3c.dom.Text
import java.io.*
import java.nio.Buffer
import java.nio.ByteBuffer
import java.security.Permission
import java.util.concurrent.TimeUnit
import java.util.logging.Handler


class CaptureAkarin : AppCompatActivity() {

    private lateinit var file : File
    var mBackgroundThread : HandlerThread? = null
    var mBackgroundHandler : android.os.Handler? = null
    var cameraDevice : CameraDevice? = null
    val TAG : String = "Akarin Camera : "
    val REQUEST_CAMERA_PERMISSION : Int = 200
    private lateinit var texture : TextureView
    lateinit var imageDimension : Size
    companion object{
        val ORIENTATIONS : SparseIntArray = SparseIntArray()
        init{
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
    var captureBuilder : CaptureRequest.Builder? = null
    var cameraCaptureSessions : CameraCaptureSession? = null
    var imageReader : ImageReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_akarin)
        Log.e(TAG, "onCreate")
        texture = findViewById(R.id.txtureView)
        assert(texture != null)
        texture.surfaceTextureListener = textureListener
        Log.v(TAG, texture.surfaceTextureListener.toString())
    }

    fun capture(view: View) {
        takePicture()
    }

    val textureListener = object : TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera()
        }
        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) { }
        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = false
        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit
    }

    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.e(TAG, "onOpened")
            cameraDevice = camera
            createCameraPreview()
        }
        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
        }
        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
        }
    }

    private val captureCallbackListener = object : CameraCaptureSession.CaptureCallback(){
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)
            Toast.makeText(this@CaptureAkarin, "Saved: " + file, Toast.LENGTH_SHORT).show()
            createCameraPreview()
        }
    }

    fun startBackgroundThread(){
        Log.d(TAG, "ThreadParsed")
        val backgroundThread : HandlerThread = HandlerThread("Camera Background")
        mBackgroundThread = backgroundThread
        backgroundThread.start()
    }

    fun stopBackgroundThread(){
        mBackgroundThread?.quitSafely()
        try{
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e : InterruptedException){
            e.printStackTrace()
        }
    }

    fun takePicture(){
        if(cameraDevice == null){
            Log.e(TAG, "cameraDevice is Null")
            return
        }
        val manager : CameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try{
            val characteristics : CameraCharacteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
            lateinit var jpegSizes : Array<Size>
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.getOutputSizes(ImageFormat.JPEG)
            }
            var width: Int = 640
            var height : Int = 480
            if(0 < jpegSizes.size){
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            val reader : ImageReader = ImageReader.newInstance(width,height, ImageFormat.JPEG, 1)
            val outputSurfaces : ArrayList<Surface> = ArrayList<Surface>(2)
            outputSurfaces.add(reader.surface)
            outputSurfaces.add(Surface(texture.surfaceTexture))
            captureBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder?.addTarget(reader.surface)
            captureBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            // Orientation
            val rotation : Int = windowManager.defaultDisplay.rotation
            captureBuilder?.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            file = File(Environment.getExternalStorageState() + "/DCIM/IMG/pic.jpg")
            lateinit var bytes : ByteArray
            val readerListener = object : ImageReader.OnImageAvailableListener{
                override fun onImageAvailable(reader: ImageReader?) {
                    var image: Image? = null
                    try{
                        image = reader?.acquireLatestImage()
                        lateinit var buffer : ByteBuffer
                        if(image != null) buffer = image.planes[0].buffer
                        bytes = ByteArray(buffer.capacity())
                        buffer.get(bytes)
                    } catch (e : FileNotFoundException){
                        e.printStackTrace()
                    } catch (e : IOException){
                        e.printStackTrace()
                    } finally {
                        if(image != null) image.close()
                    }
                }
            }
            var output : OutputStream? = null
            try{
                output = FileOutputStream(file)
                output.write(bytes)
            } catch (e: IOException){
                e.printStackTrace()
            } finally {
                if(output != null) output.close()
            }
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler)
            val capturListener = object : CameraCaptureSession.CaptureCallback(){
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    Toast.makeText(this@CaptureAkarin, "Saved: " + file, Toast.LENGTH_SHORT).show()
                    createCameraPreview()
                }
            }
            val session = object: CameraCaptureSession.StateCallback(){
                override fun onConfigured(session: CameraCaptureSession) {
                    try{
                        session.capture(captureBuilder?.build()!!, capturListener, mBackgroundHandler)
                    } catch (e: CameraAccessException){
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            cameraDevice?.createCaptureSession(outputSurfaces, session, mBackgroundHandler)
        } catch (e : CameraAccessException){
            e.printStackTrace()
        }

    }

    fun createCameraPreview(){
        try{
            val textureView : SurfaceTexture = texture.surfaceTexture
            textureView.setDefaultBufferSize(imageDimension.width, imageDimension.height)
            val surface : Surface = Surface(textureView)
            captureBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureBuilder?.addTarget(surface)
            val list = arrayListOf<Surface>(surface)
            val session = object : CameraCaptureSession.StateCallback(){
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    if(cameraDevice == null) return
                    cameraCaptureSessions = cameraCaptureSession
                    updatePreview()
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    Toast.makeText(this@CaptureAkarin, "Configuration change", Toast.LENGTH_SHORT).show()
                }
            }
            cameraDevice?.createCaptureSession(list, session, null)
        } catch (e: CameraAccessException){
            e.printStackTrace()
        }
    }

    fun openCamera(){
        val manager : CameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.e(TAG, "is camera open")
        try{
            val cameraId: String = manager.cameraIdList[0]
            val characteristics : CameraCharacteristics = manager.getCameraCharacteristics(cameraId)
            val map : StreamConfigurationMap? = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            imageDimension = map!!.getOutputSizes(SurfaceTexture::class.java)[0]
            // Permission
            if(((ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED)) &&
                ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED))){
                val permission  = arrayOf(CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                ActivityCompat.requestPermissions(
                    this,
                    permission,
                    REQUEST_CAMERA_PERMISSION
                )
                return
            }
            manager.openCamera(cameraId, cameraStateCallback, null)
        } catch (e : CameraAccessException){
            e.printStackTrace()
        }
        Log.e(TAG, "openCamera X")
    }
    fun updatePreview(){
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions?.setRepeatingRequest(captureBuilder!!.build(), null, mBackgroundHandler);
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }

    fun closeCamera(){
        if(cameraDevice != null){
            cameraDevice?.close()
            cameraDevice = null
        }
        if(imageReader != null){
            imageReader?.close()
            imageReader = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this@CaptureAkarin, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        startBackgroundThread()
        if(texture.isAvailable){
            Log.d(TAG, "Normal")
            openCamera()
        } else{
            Log.d(TAG, "Problem")
            texture.surfaceTextureListener = textureListener
        }
    }

    override fun onPause() {
        Log.e(TAG, "onPause")
        stopBackgroundThread()
        super.onPause()
    }
}