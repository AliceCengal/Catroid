/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.livewallpaper;

import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.Values;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.utils.ImageEditing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class WallpaperLook {

	private LookData lookData;
	private Sprite sprite;
	private Bitmap look = null;
	private Matrix matrix;
	private Paint paint;

	private int x;
	private int y;

	private int centerX;
	private int centerY;

	private int lookWidth;
	private int lookHeight;

	float rotation = 0f;

	private int zPosition;

	private int alphaValue = 255;
	private float brightness = 0;

	private double size = 1;

	private boolean hidden = false;
	private boolean isBackground = false;
	private boolean isLandscape = false;
	private boolean changeMatrix = true;

	private int landscapeRotation;

	private WallpaperHelper wallpaperHelper;

	public WallpaperLook(Sprite sprite, LookData lookData) {

		this.wallpaperHelper = WallpaperHelper.getInstance();
		this.sprite = sprite;
		this.zPosition = wallpaperHelper.getProject().getSpriteList()
				.indexOf(sprite);
		this.matrix = new Matrix();

		if (sprite.getName().equals("Background")) {
			this.isBackground = true;
		}

		this.x = 0;
		this.y = 0;

		this.paint = new Paint();

		if (lookData != null) {
			setLook(lookData);
		}

		if (wallpaperHelper.isLandscape()) {
			int temp = x;
			x = y;
			y = temp;

			this.isLandscape = true;
			this.landscapeRotation = wallpaperHelper
					.getLandscapeRotationDegree();

		}

		sprite.setWallpaperLook(this);

	}

	public void clear() {
		alphaValue = 255;
		brightness = 1f;
		rotation = 0f;
		size = 1;
		hidden = false;
		zPosition = wallpaperHelper.getProject().getSpriteList()
				.indexOf(sprite);
		look = null;
		if (lookData != null) {
			lookData.nullifyBitmaps();
		}
	}

	private void updateMatrix() {

		if (changeMatrix) {

			matrix.setRotate(rotation, lookWidth / 2, lookHeight / 2);
			matrix.postScale((float) size, (float) size);

			if (isLandscape) {
				centerX = wallpaperHelper.getCenterXCoord() - y
						- (int) (lookWidth * size) / 2;
				centerY = wallpaperHelper.getCenterYCoord() - x
						- (int) (lookHeight * size) / 2;
			} else {
				centerX = wallpaperHelper.getCenterXCoord() + x
						- (int) (lookWidth * size) / 2;
				centerY = wallpaperHelper.getCenterYCoord() - y
						- (int) (lookHeight * size) / 2;
			}
		}

	}

	public void setX(int x) {
		if (isLandscape && landscapeRotation == 90) {
			this.y = -x;
		} else if (isLandscape && landscapeRotation == -90) {
			this.y = x;
		} else {
			this.x = x;
		}
		changeMatrix = true;
	}

	public void setY(int y) {
		if (isLandscape && landscapeRotation == 90) {
			this.x = -y;
		} else if (isLandscape && landscapeRotation == -90) {
			this.x = y;
		} else {
			this.y = y;
		}

		changeMatrix = true;
	}

	public void changeXBy(int x) {
		if (isLandscape && landscapeRotation == 90) {
			this.y -= x;
		} else if (isLandscape && landscapeRotation == -90) {
			this.y += x;
		} else {
			this.x += x;
		}

		changeMatrix = true;

	}

	public void changeYby(int y) {
		if (isLandscape && landscapeRotation == 90) {
			this.x -= y;
		} else if (isLandscape && landscapeRotation == -90) {
			this.x += y;
		} else {
			this.y += y;
		}
		changeMatrix = true;
	}

	public boolean touchedInsideTheLook(float touchX, float touchY) {
		if (isBackground || look == null) {
			return false;
		}

		float right = centerX;
		float bottom = centerY;

		right += lookWidth;
		bottom += lookHeight;

		if (touchX > centerX && touchX < right && touchY > centerY
				&& touchY < bottom) {
			return true;
		}

		return false;

	}

	public Bitmap getLook() {
		if (isBackground && look == null) {
			look = ImageEditing.createSingleColorBitmap(Values.SCREEN_WIDTH,
					Values.SCREEN_HEIGHT, Color.WHITE);
			setX(-360);
			setY(592);
		}

		return look;
	}

	public void setLook(LookData lookData) {
		this.lookData = lookData;
		this.look = lookData.getLookBitmap();
		this.lookWidth = look.getWidth();
		this.lookHeight = look.getHeight();
		changeMatrix = true;

	}

	public void setLookSize(double size) {
		this.size = size * 0.01;
		changeMatrix = true;
	}

	public void changeLookSizeBy(double changeValue) {
		this.size += (changeValue * 0.01);
		if (this.size < 0) {
			this.size = 0;
		}

		changeMatrix = true;
	}

	public LookData getLookData() {
		return lookData;
	}

	public boolean isLookHidden() {
		return hidden;
	}

	public void setLookHidden(boolean hideLook) {
		this.hidden = hideLook;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean isBackground() {
		return isBackground;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
		ColorMatrix cm = new ColorMatrix();
		cm.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0,
				0, 1, 0, brightness, 0, 0, 0, alphaValue, 0 });

		paint.setColorFilter(new ColorMatrixColorFilter(cm));
	}

	public void clearGraphicEffect() {
		paint = new Paint();
	}

	public int getzPosition() {
		return zPosition;
	}

	public void setzPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(float r) {
		this.rotation = r;
		changeMatrix = true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Matrix getMatrix() {
		updateMatrix();
		return matrix;
	}

	public Paint getPaint() {
		return paint;
	}

	public int getAlphaValue() {
		return alphaValue;
	}

	public void setAlphaValue(int alphaValue) {
		this.alphaValue = alphaValue;
		paint.setAlpha(alphaValue);
	}

	public void rotate(float r) {
		this.rotation += r;
		changeMatrix = true;
	}

}
