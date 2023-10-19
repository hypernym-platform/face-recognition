package com.hypernym.smartsurveillance.face_detection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.Image
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.hypernym.smartsurveillance.R.*
import com.hypernym.smartsurveillance.camerax.BaseImageAnalyzer
import com.hypernym.smartsurveillance.camerax.GraphicOverlay
import com.hypernym.smartsurveillance.helper.ImageHelper
import com.hypernym.smartsurveillance.utilities.AppConstants
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.utilities.DateTimeUtils
import com.hypernym.smartsurveillance.view.fragment.HomeFragment
import com.hypernym.smartsurveillance.view.fragment.PauseFragment
import com.microsoft.projectoxford.face.FaceServiceClient
import com.microsoft.projectoxford.face.FaceServiceClient.FaceAttributeType
import com.microsoft.projectoxford.face.FaceServiceRestClient
import com.microsoft.projectoxford.face.contract.Person
import com.microsoft.projectoxford.face.contract.TrainingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class FaceContourDetectionProcessor(
    private val context: Context,
    private val view: GraphicOverlay
) :
    BaseImageAnalyzer<List<Face>>() {


    private var exist: Boolean=false
    private var faceid: UUID? = null
    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .build()

    var isProcessing: Boolean = false
    val mainLooperHandler = Handler(Looper.getMainLooper())

    // var mPersonGroupId: String? = "5f20168b-b2e1-42a0-890b-a36e9c458f1b"
    // val mPersonGroupId: String = "fd93c6b2-0909-44f5-994e-5077d9d2d984"

    var mPersonGroupId = AppUtils.getLatestPersonGroupId()

    var detected = false

    var result: Bitmap? = null

    var screenSaverTimer = 10000L


    // Get an instance of face service client.
    val faceServiceClient: FaceServiceClient = FaceServiceRestClient(
        AppConstants.AZURE_ENDPOINT,
        AppConstants.AZURE_SUBCRIPTION_KEY
    )

    var azureDetectResult: Array<com.microsoft.projectoxford.face.contract.Face>? = null
    var azureIdentificationResult: Array<com.microsoft.projectoxford.face.contract.IdentifyResult>? =
        null
    var azurePersonResult: Person? = null


    private val detector = FaceDetection.getClient(realTimeOpts)


    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(inputimage: InputImage, image: Image): Task<List<Face>> {
        screenSaverWatcher()
        result = ImageHelper.RotateBitmap(AppUtils.convertImagetoBitmap(image), 270f)

        AppUtils.originalBitmap = result



        return detector.process(inputimage)
    }

    override fun stop() {
        try {

            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        graphicOverlay.clear()
        results.forEach {
            val faceGraphic = FaceContourGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(faceGraphic)
        }
        if (!results.isEmpty()) {
            mainLooperHandler.removeCallbacksAndMessages(null)

            if (AppUtils.isProcessing == false) {

                Log.e("onSuccess", "Detect face")
                detect(result)

            }
        }
        graphicOverlay.postInvalidate()
    }


    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }


    override fun detect(mBitmap: Bitmap?) {
        // Put the image into an input stream for detection.
        AppUtils.isProcessing = true
        val output = ByteArrayOutputStream()
        mBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, output)
        val inputStream = ByteArrayInputStream(output.toByteArray())


        GlobalScope.launch {
            detectImage(inputStream)
        }

        // DetectionTask().execute(inputStream)
    }


    private suspend fun detectImage(inputStream: InputStream) {
        var mSucceed = true
        // Model inferencing is CPU internsive task so run it on the Default dispatcher
        withContext(Dispatchers.IO) {
            // withContext waits for all children coroutines
            // Get an instance of face service client to detect faces in image.

            try {
                //  publishProgress("Detecting...")

                // Start detection.

                azureDetectResult = faceServiceClient.detect(
                    inputStream,  /* Input stream of image to detect */
                    true,  /* Whether to return face ID */
                    true, arrayOf(
                        FaceAttributeType.Age,
                        FaceAttributeType.Gender,
                        FaceAttributeType.Smile,
                        FaceAttributeType.Glasses,
                        FaceAttributeType.FacialHair,
                        FaceAttributeType.Emotion,
                        FaceAttributeType.HeadPose,
                        FaceAttributeType.Accessories,
                        FaceAttributeType.Blur,
                        FaceAttributeType.Exposure,
                        FaceAttributeType.Hair,
                        FaceAttributeType.Makeup,
                        FaceAttributeType.Noise,
                        FaceAttributeType.Occlusion
                    )
                )


            } catch (e: java.lang.Exception) {
                mSucceed = false
                Log.e(TAG, "Response: Failure.$e")

            }


            // Since we are updating the UI, do the operation on the Main dispatcher
            withContext(Dispatchers.Main) {
                // withContext waits for all children coroutines
                if (mSucceed) {

                    AppUtils.bitmap = ImageHelper.drawFaceRectanglesOnBitmap(
                        AppUtils.originalBitmap,
                        azureDetectResult,
                        true
                    )

                    Log.e(
                        TAG,
                        "Response: Success. Detected " + (azureDetectResult?.size) + " face(s) in "
                    )

                    if (azureDetectResult?.size == 0) {
                        detected = false
                        AppUtils.isProcessing = false
                        setInfo("No faces detected!")

                    } else {
                        detected = true
                        identify()

                    }
                }


            }


        }
    }

    // Called when the "Detect" button is clicked.
    suspend fun identify() {
        // Start detection task only if the image to detect is selected.
        if (detected && mPersonGroupId != null) {
            // Start a background task to identify faces in the image.
            val faceIds: MutableList<UUID> = ArrayList()
            for (face in azureDetectResult!!) {
                faceIds.add(face.faceId)
            }
            // setAllButtonsEnabledStatus(false)

            var mSucceed = true
            // Model inferencing is CPU internsive task so run it on the Default dispatcher
            withContext(Dispatchers.IO) {
                AppUtils.info = "Request: Identifying faces "
                for (faceId in azureDetectResult!!) {
                    AppUtils.info += "$faceId, "
                }
                AppUtils.info += " in group $mPersonGroupId"


                // Get an instance of face service client to detect faces in image.

                try {
                    AppUtils.info = ("Getting person group status...")
                    val trainingStatus = faceServiceClient.getLargePersonGroupTrainingStatus(
                        mPersonGroupId
                    ) /* personGroupId */
                    if (trainingStatus.status != TrainingStatus.Status.Succeeded) {
                        AppUtils.info = ("Person group training status is " + trainingStatus.status)
                        mSucceed = false
                        azureIdentificationResult = null
                    }
                    AppUtils.info = ("Identifying...")

                    // Start identification.
                    azureIdentificationResult = faceServiceClient.identityInLargePersonGroup(
                        mPersonGroupId,  /* personGroupId */
                        faceIds.toTypedArray(),  /* faceIds */
                        1
                    ) /* maxNumOfCandidatesReturned */
                } catch (e: java.lang.Exception) {
                    mSucceed = false
                    AppUtils.info = (e.message)
                    null
                }

                if (azureIdentificationResult != null) {
                    for (identifyResult in azureIdentificationResult!!) {
                        AppUtils.info += ("Face " + identifyResult.faceId.toString() + " is identified as "
                                + (if (identifyResult.candidates.size > 0) {
                            identifyResult.candidates[0].personId.toString()
                        } else {
                            "Unknown Person"
                        }) + ". ")
                    }

                    if (azureIdentificationResult!!.get(0).candidates.size > 0) {
                        getPersonNameFromAzure()
                    } else {
                        AppUtils.displaySnackbar(context, "Unidentified person")
                        Handler(Looper.getMainLooper()).postDelayed({

                            AppUtils.isProcessing = false

                        }, 1000)
                    }

                } else {
                    AppUtils.info = "Unidentified person"

                    AppUtils.isProcessing = false
                }


            }

        } else {
            // Not detected or person group exists.
            setInfo("Please select an image and create a person group first.")
        }
    }


    private fun setInfo(info: String) {

        AppUtils.info = info
    }

    private suspend fun getPersonNameFromAzure() {

        withContext(Dispatchers.IO) {


            try {
                faceid = azureIdentificationResult?.get(0)?.candidates!![0].personId
                azurePersonResult = faceServiceClient.getPersonInLargePersonGroup(mPersonGroupId, faceid)
                //  azurePersonResult = faceServiceClient.getPersonInLargePersonGroup(mPersonGroupId,faceid)

                if (azurePersonResult != null) {

                    AppUtils.info =
                        ("Face " + azureIdentificationResult?.get(0)?.candidates!![0].personId.toString() + " is identified as "
                                + azurePersonResult?.name.toString())
                    val message = "Detected: " + azurePersonResult?.name.toString()

                    val file = ImageHelper.drawFaceRectanglesOnBitmap(
                        AppUtils.bitmap,
                        azureDetectResult,
                        true
                    )

                    AppUtils.displaySnackbar(context, message)
                    AppUtils.attendenceList.forEach {
                        exist =  AppUtils.attendenceList.contains(azurePersonResult?.name.toString())
                    }
                    if(!exist) {
                        AppUtils.attendenceList.add(azurePersonResult?.name.toString())
                        AppUtils.attendenceTime.add(DateTimeUtils.getCurrentDateTime(false,true))
                    }else{
                        return@withContext
                    }

                } else {
                    AppUtils.info = "Couldn't get person name"
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                AppUtils.info = (e.message)
                null
            }



        }
        Handler(Looper.getMainLooper()).postDelayed({

            AppUtils.isProcessing = false

        }, 0)
    }



    private fun screenSaverWatcher() {


        mainLooperHandler.postDelayed({
            setInfo("")
            //  if (AppUtils.screenSaver == true){
            //   AppUtils.screenSaver=false
            mainLooperHandler.removeCallbacksAndMessages(null)
            try {
                context?.let {
                    replaceFragment(
                        id.layout_container, PauseFragment(),
                        it, false, null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //   }


        }, screenSaverTimer)
    }


    companion object {
        private const val TAG = "FaceDetectorProcessor"

    }

}
