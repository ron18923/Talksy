package com.example.talksy.presentation.reusable.nonComposables

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

// TODO: later implement image compression
//fun compressAndSetImage(result: Uri, context: Context){
//    val job = Job()
//    val uiScope = CoroutineScope(Dispatchers.IO + job)
//    val fileUri = getFilePathFromUri(result, context!!)
//    uiScope.launch {
//        val compressedImageFile = Compressor.compress(context!!, File(fileUri.path)){
//            `quality(50) // combine with compressor constraint
//            format(Bitmap.CompressFormat.JPEG)
//        }
//        resultUri = Uri.fromFile(compressedImageFile)
//
//        activity!!.runOnUiThread {
//            resultUri?.let {
//                //set image here
//            }
//        }
//    }
//}
//
//@Throws(IOException::class)
//fun getFilePathFromUri(uri: Uri?, context: Context?): Uri? {
//    val fileName: String? = getFileName(uri, context)
//    val file = File(context?.externalCacheDir, fileName)
//    file.createNewFile()
//    FileOutputStream(file).use { outputStream ->
//        context?.contentResolver?.openInputStream(uri).use { inputStream ->
//            copyFile(inputStream, outputStream)
//            outputStream.flush()
//        }
//    }
//    return Uri.fromFile(file)
//}
//
//@Throws(IOException::class)
//private fun copyFile(`in`: InputStream?, out: OutputStream) {
//    val buffer = ByteArray(1024)
//    var read: Int? = null
//    while (`in`?.read(buffer).also({ read = it!! }) != -1) {
//        read?.let { out.write(buffer, 0, it) }
//    }
//}//copyFile ends
//
//fun getFileName(uri: Uri?, context: Context?): String? {
//    var fileName: String? = getFileNameFromCursor(uri, context)
//    if (fileName == null) {
//        val fileExtension: String? = getFileExtension(uri, context)
//        fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
//    } else if (!fileName.contains(".")) {
//        val fileExtension: String? = getFileExtension(uri, context)
//        fileName = "$fileName.$fileExtension"
//    }
//    return fileName
//}
//
//fun getFileExtension(uri: Uri?, context: Context?): String? {
//    val fileType: String? = context?.contentResolver?.getType(uri)
//    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
//}
//
//fun getFileNameFromCursor(uri: Uri?, context: Context?): String? {
//    val fileCursor: Cursor? = context?.contentResolver
//        ?.query(uri, arrayOf<String>(OpenableColumns.DISPLAY_NAME), null, null, null)
//    var fileName: String? = null
//    if (fileCursor != null && fileCursor.moveToFirst()) {
//        val cIndex: Int = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        if (cIndex != -1) {
//            fileName = fileCursor.getString(cIndex)
//        }
//    }
//    return fileName
//}