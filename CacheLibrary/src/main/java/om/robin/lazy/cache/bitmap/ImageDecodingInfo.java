/*******************************************************************************
 * Copyright 2013-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package om.robin.lazy.cache.bitmap;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory.Options;
import android.os.Build;

/**
 * 图片解码的一些信息
 * Contains needed information for decoding image to Bitmap
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.3
 */
public class ImageDecodingInfo {

	private final ImageSize targetSize;

	private final ImageScaleType imageScaleType;
	private final ViewScaleType viewScaleType;

	private final boolean considerExifParams;
	private final Options decodingOptions;

	public ImageDecodingInfo(ImageSize targetSize,ImageScaleType imageScaleType, ViewScaleType viewScaleType, boolean considerExifParams,Options options) {
		this.targetSize = targetSize;

		this.imageScaleType = imageScaleType;
		this.viewScaleType = viewScaleType;
		this.considerExifParams = considerExifParams;
		decodingOptions = new Options();
		copyOptions(decodingOptions, options);
	}

	private void copyOptions(Options srcOptions, Options destOptions) {
		destOptions.inDensity = srcOptions.inDensity;
		destOptions.inDither = srcOptions.inDither;
		destOptions.inInputShareable = srcOptions.inInputShareable;
		destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
		destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
		destOptions.inPurgeable = srcOptions.inPurgeable;
		destOptions.inSampleSize = srcOptions.inSampleSize;
		destOptions.inScaled = srcOptions.inScaled;
		destOptions.inScreenDensity = srcOptions.inScreenDensity;
		destOptions.inTargetDensity = srcOptions.inTargetDensity;
		destOptions.inTempStorage = srcOptions.inTempStorage;
		if (Build.VERSION.SDK_INT >= 10) copyOptions10(srcOptions, destOptions);
		if (Build.VERSION.SDK_INT >= 11) copyOptions11(srcOptions, destOptions);
	}

	@TargetApi(10)
	private void copyOptions10(Options srcOptions, Options destOptions) {
		destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
	}

	@TargetApi(11)
	private void copyOptions11(Options srcOptions, Options destOptions) {
		destOptions.inBitmap = srcOptions.inBitmap;
		destOptions.inMutable = srcOptions.inMutable;
	}

	/**
	 * @return Target size for image. Decoded bitmap should close to this size according to {@linkplain ImageScaleType
	 * image scale type} and {@linkplain ViewScaleType view scale type}.
	 */
	public ImageSize getTargetSize() {
		return targetSize;
	}

	/**
	 * @return {@linkplain ImageScaleType Scale type for image sampling and scaling}. This parameter affects result size
	 * of decoded bitmap.
	 */
	public ImageScaleType getImageScaleType() {
		return imageScaleType;
	}

	/** @return {@linkplain ViewScaleType View scale type}. This parameter affects result size of decoded bitmap. */
	public ViewScaleType getViewScaleType() {
		return viewScaleType;
	}

	/** @return <b>true</b> - if EXIF params of image should be considered; <b>false</b> - otherwise */
	public boolean shouldConsiderExifParams() {
		return considerExifParams;
	}

	/** @return Decoding options */
	public Options getDecodingOptions() {
		return decodingOptions;
	}
}