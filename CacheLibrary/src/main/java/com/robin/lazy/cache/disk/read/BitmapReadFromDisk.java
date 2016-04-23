/*
 * 文 件 名:  BitmapReadFromDisk.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月11日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.read;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.IoUtils;
import com.robin.lazy.util.bitmap.ImageDecodingInfo;
import com.robin.lazy.util.bitmap.ImageScaleType;
import com.robin.lazy.util.bitmap.ImageSize;
import com.robin.lazy.util.bitmap.ImageSizeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 从文件中读取图片
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BitmapReadFromDisk implements ReadFromDisk<Bitmap> {

	private ImageDecodingInfo imDecodeInfor;

	public BitmapReadFromDisk(ImageDecodingInfo imDecodeInfor) {
		this.imDecodeInfor = imDecodeInfor;
	}

	@Override
	public Bitmap readOut(File file) {
		Bitmap decodedBitmap = null;
		ImageFileInfo imageInfo = null;
		InputStream imageStream = null;
		try {
			imageStream = getImageStream(file);
			imageInfo = defineImageSizeAndRotation(file.getAbsolutePath(),
					imageStream, imDecodeInfor);
			imageStream = resetStream(file, imageStream, imDecodeInfor);
			Options decodingOptions = prepareDecodingOptions(
					imageInfo.imageSize, imDecodeInfor);
			decodedBitmap = BitmapFactory.decodeStream(imageStream, null,
					decodingOptions);
		} catch (IOException e) {
		} finally {
			IoUtils.closeSilently(imageStream);
		}
		if (decodedBitmap == null || imageInfo == null) {
			LazyLogger.e("Image can't be decoded [%s]");
		} else {
			decodedBitmap = considerExactScaleAndOrientatiton(decodedBitmap,
					imDecodeInfor, imageInfo.exif.rotation,
					imageInfo.exif.flipHorizontal);
		}
		return decodedBitmap;
	}

	/***
	 * 获取文件流
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 *             InputStream
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	private InputStream getImageStream(File file) throws IOException {
		return new FileInputStream(file);
	}

	protected Options prepareDecodingOptions(ImageSize imageSize,
			ImageDecodingInfo decodingInfo) {
		ImageScaleType scaleType = decodingInfo.getImageScaleType();
		int scale;
		if (scaleType == ImageScaleType.NONE) {
			scale = 1;
		} else if (scaleType == ImageScaleType.NONE_SAFE) {
			scale = ImageSizeUtils.computeMinImageSampleSize(imageSize);
		} else {
			ImageSize targetSize = decodingInfo.getTargetSize();
			boolean powerOf2 = scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2;
			scale = ImageSizeUtils.computeImageSampleSize(imageSize,
					targetSize, decodingInfo.getViewScaleType(), powerOf2);
		}
		Options decodingOptions = decodingInfo.getDecodingOptions();
		decodingOptions.inSampleSize = scale;
		return decodingOptions;
	}

	private InputStream resetStream(File fileName, InputStream imageStream,
			ImageDecodingInfo decodingInfo) throws IOException {
		try {
			imageStream.reset();
		} catch (IOException e) {
			IoUtils.closeSilently(imageStream);
			imageStream = getImageStream(fileName);
		}
		return imageStream;
	}

	private boolean canDefineExifParams(String mimeType) {
		return "image/jpeg".equalsIgnoreCase(mimeType);
	}

	private ExifInfo defineExifOrientation(String filePath) {
		int rotation = 0;
		boolean flip = false;
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (exifOrientation) {
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL :
					flip = true;
				case ExifInterface.ORIENTATION_NORMAL :
					rotation = 0;
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE :
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_90 :
					rotation = 90;
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL :
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_180 :
					rotation = 180;
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE :
					flip = true;
				case ExifInterface.ORIENTATION_ROTATE_270 :
					rotation = 270;
					break;
			}
		} catch (IOException e) {
			LazyLogger.e("e", "Can't read EXIF tags from file [%s]", filePath);
		}
		return new ExifInfo(rotation, flip);
	}

	private ImageFileInfo defineImageSizeAndRotation(String filePath,
			InputStream imageStream, ImageDecodingInfo decodingInfo)
			throws IOException {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(imageStream, null, options);

		ExifInfo exif;
		if (decodingInfo.shouldConsiderExifParams()
				&& canDefineExifParams(options.outMimeType)) {
			exif = defineExifOrientation(filePath);
		} else {
			exif = new ExifInfo();
		}
		return new ImageFileInfo(new ImageSize(options.outWidth,
				options.outHeight, exif.rotation), exif);
	}

	private Bitmap considerExactScaleAndOrientatiton(Bitmap subsampledBitmap,
			ImageDecodingInfo decodingInfo, int rotation, boolean flipHorizontal) {
		Matrix m = new Matrix();
		// Scale to exact size if need
		ImageScaleType scaleType = decodingInfo.getImageScaleType();
		if (scaleType == ImageScaleType.EXACTLY
				|| scaleType == ImageScaleType.EXACTLY_STRETCHED) {
			ImageSize srcSize = new ImageSize(subsampledBitmap.getWidth(),
					subsampledBitmap.getHeight(), rotation);
			float scale = ImageSizeUtils.computeImageScale(srcSize,
					decodingInfo.getTargetSize(),
					decodingInfo.getViewScaleType(),
					scaleType == ImageScaleType.EXACTLY_STRETCHED);
			if (Float.compare(scale, 1f) != 0) {
				m.setScale(scale, scale);
			}
		}
		// Flip bitmap if need
		if (flipHorizontal) {
			m.postScale(-1, 1);
		}
		// Rotate bitmap if need
		if (rotation != 0) {
			m.postRotate(rotation);
		}

		Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0,
				subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m,
				true);
		if (finalBitmap != subsampledBitmap) {
			subsampledBitmap.recycle();
		}
		return finalBitmap;
	}

	private static class ExifInfo {

		public final int rotation;
		public final boolean flipHorizontal;

		private ExifInfo() {
			this.rotation = 0;
			this.flipHorizontal = false;
		}

		private ExifInfo(int rotation, boolean flipHorizontal) {
			this.rotation = rotation;
			this.flipHorizontal = flipHorizontal;
		}
	}

	private static class ImageFileInfo {

		public final ImageSize imageSize;
		public final ExifInfo exif;

		private ImageFileInfo(ImageSize imageSize, ExifInfo exif) {
			this.imageSize = imageSize;
			this.exif = exif;
		}
	}
}
