/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.common;

import java.io.Serializable;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.livewallpaper.WallpaperHelper;
import org.catrobat.catroid.utils.ImageEditing;
import org.catrobat.catroid.utils.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LookData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String fileName;
	private transient Bitmap thumbnailBitmap;
	private transient Integer width;
	private transient Integer height;
	private transient static final int THUMBNAIL_WIDTH = 150;
	private transient static final int THUMBNAIL_HEIGHT = 150;
	private transient Pixmap pixmap = null;
	private transient Pixmap originalPixmap = null;
	private transient TextureRegion region = null;

	private Bitmap lookBitmap;
	private Bitmap landscapeLookBitmap;

	public TextureRegion getTextureRegion() {
		if (region == null) {
			region = new TextureRegion(new Texture(getPixmap()));
		}
		return region;
	}

	public void setTextureRegion() {
		this.region = new TextureRegion(new Texture(getPixmap()));
	}

	public Pixmap getPixmap() {
		if (pixmap == null) {
			if (Utils.isLoadingFromAssetsNecessary()) {
				pixmap = new Pixmap(Gdx.files.internal(getAbsolutePath()));//No absolute path to assets possible.
			} else {
				pixmap = new Pixmap(Gdx.files.absolute(getAbsolutePath()));
			}
		}
		return pixmap;
	}

	public void setPixmap(Pixmap pixmap) {
		this.pixmap = pixmap;
	}

	public Pixmap getOriginalPixmap() {
		if (originalPixmap == null) {
			if (Utils.isLoadingFromAssetsNecessary()) {
				originalPixmap = new Pixmap(Gdx.files.internal(getAbsolutePath()));//No absolute path to assets possible.
			} else {
				originalPixmap = new Pixmap(Gdx.files.absolute(getAbsolutePath()));
			}
		}
		return originalPixmap;

	}

	public LookData() {
	}

	public String getAbsolutePath() {
		String path;
		if (fileName != null) {
			if (Utils.isLoadingFromAssetsNecessary()) {
				path = Constants.IMAGE_DIRECTORY + '/' + fileName;//Path has to be relative.
			} else {
				path = Utils.buildPath(getPathToImageDirectory(), fileName);
			}
		} else {
			return null;
		}
		return path;
	}

	public String getLookName() {
		return name;
	}

	public void setLookName(String name) {
		this.name = name;
	}

	public void setLookFilename(String fileName) {
		this.fileName = fileName;
	}

	public String getLookFileName() {
		return fileName;
	}

	public String getChecksum() {
		if (fileName == null) {
			return null;
		}
		return fileName.substring(0, 32);
	}

	private String getPathToImageDirectory() {
		String path = null;
		if (Utils.isLoadingFromAssetsNecessary()) {
			path = Constants.IMAGE_DIRECTORY;//Root is automatically asset folder.
		} else {
			path = Utils.buildPath(Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
					Constants.IMAGE_DIRECTORY);
		}
		return path;
	}

	public Bitmap getThumbnailBitmap() {
		if (thumbnailBitmap == null) {
			thumbnailBitmap = ImageEditing.getScaledBitmapFromPath(getAbsolutePath(), THUMBNAIL_HEIGHT,
					THUMBNAIL_WIDTH, false);
		}
		return thumbnailBitmap;
	}

	public void resetThumbnailBitmap() {
		thumbnailBitmap = null;
	}

	public Bitmap getLookBitmap() {
		if (lookBitmap == null) {
			//CODE FOR LOADING FROM SD CARD
			lookBitmap = BitmapFactory.decodeFile(getAbsolutePath());
			//____________________________________________________

			//CODE FOR LOADING FROM ASSETS
			//			try {
			//				InputStream istr = LiveWallpaper.getContext().getAssets().open(getPath());
			//				lookBitmap = BitmapFactory.decodeStream(istr);
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
			//________________________________________________________________________________

		}

		WallpaperHelper wallpaperHelper = WallpaperHelper.getInstance();

		if (wallpaperHelper.isLandscape()) {
			if (landscapeLookBitmap == null) {
				landscapeLookBitmap = ImageEditing.rotateBitmap(lookBitmap,
						wallpaperHelper.getLandscapeRotationDegree());
			}
			return landscapeLookBitmap;
		} else {
			return lookBitmap;
		}
	}

	public void nullifyBitmaps() {
		if (lookBitmap != null) {
			lookBitmap = null;
		}

		if (landscapeLookBitmap != null) {
			landscapeLookBitmap = null;
		}
	}

	public int[] getResolution() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(getAbsolutePath(), options);
		width = options.outWidth;
		height = options.outHeight;

		return new int[] { width, height };
	}

	@Override
	public String toString() {
		return name;
	}
}