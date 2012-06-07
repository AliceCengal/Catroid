/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.tutorial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import at.tugraz.ist.catroid.tutorial.tasks.Task;

/**
 * @author faxxe
 * 
 */
public class Tutor extends SurfaceObjectTutor implements SurfaceObject {

	private Context context;
	private TutorialOverlay tutorialOverlay;
	private Resources ressources;
	private Bitmap bitmap;
	private int state = -1;
	private int currentFrame = 0;
	private int currentStep = 0;
	private Paint paint;
	private int sizeX = 110;
	private int sizeY = 102;
	private int targetX;
	private int targetY;
	private boolean flip = false;

	public Tutor(int drawable, TutorialOverlay tutorialOverlay) {
		super(Tutorial.getInstance(null).getActualContext(), tutorialOverlay);
		context = Tutorial.getInstance(null).getActualContext();
		ressources = context.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		bitmap = BitmapFactory.decodeResource(ressources, drawable, options);
		paint = new Paint();
		this.tutorialOverlay = tutorialOverlay;
	}

	public Tutor(int drawable, TutorialOverlay tutorialOverlay, int x, int y, Task.Tutor tutorType) {
		super(Tutorial.getInstance(null).getActualContext(), tutorialOverlay);
		context = Tutorial.getInstance(null).getActualContext();
		ressources = context.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		bitmap = BitmapFactory.decodeResource(ressources, drawable, options);
		paint = new Paint();
		this.tutorialOverlay = tutorialOverlay;
		this.targetX = x;
		this.targetY = y;
		super.tutorType = tutorType;

	}

	@Override
	public void flip() {
		if (flip) {
			flip = false;
		} else {
			flip = true;
		}
		state = 8;
	}

	@Override
	public void idle() {
		if (!flip) {
			state = 1;
		} else {
			state = 5;
		}
	}

	@Override
	public void say(String text) {
		Log.i("herb", "NewTutor: " + text);
		new Bubble(text, tutorialOverlay, this, targetX - 20, targetY - 90);

		if (!flip) {
			state = 2;
		} else {
			state = 6;
		}
	}

	@Override
	public void jumpTo(int x, int y) {
		targetX = x;
		targetY = y;

	}

	@Override
	public void appear(int x, int y) {
		targetX = x;
		targetY = y;
		Log.i("herb", " appear!");

		if (!flip) {
			state = 0;
		} else {
			state = 4;
		}
	}

	@Override
	public void disappear() {
		Log.i("herb", "disappearing...");

		if (!flip) {
			state = 3;
		} else {
			state = 7;
		}
	}

	@Override
	public void point() {

	}

	@Override
	public void setNewPositionAfterPort() {

	}

	@Override
	public void sayFinished() {
		state = 3;
	}

	@Override
	public void draw(Canvas canvas) {
		Bitmap todraw = Bitmap.createBitmap(bitmap, 0, 0, sizeX, sizeY);
		if (currentStep > 9) {
			currentStep = 0;
		}

		switch (state) {
			case 0: //APPEARING
				if (currentStep == 9) {
					state = 1;
					Tutorial.getInstance(null).setNotification("appear done!");
				}
				Log.i("HERB", "TUTOR: " + this.tutorType + " - appearing!");
				todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, state * sizeY, sizeX, sizeY);
				break;
			case 1: //IDLE
				if (currentStep == 9) {
					currentStep = 0;
				}
				todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, state * sizeY, sizeX, sizeY);
				Log.i("HERB", "TUTOR: " + this.tutorType + " - idle!");
				break;
			case 2: //SAYING
				todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, state * sizeY, sizeX, sizeY);
				break;
			case 3: //DISAPPEARING
				if (currentStep == 9) {
					currentStep = 0;
					state = 100;
					Tutorial.getInstance(null).setNotification("disappear done!");
					break;
				}
				Log.i("HERB", "TUTOR-disappeared!");
				todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, state * sizeY, sizeX, sizeY);
				break;

			/**
			 * Todo: Impement flipping procedure
			 */
			case 8: //FLIP
				if (currentStep == 9) {
					currentStep = 0;
					if (flip) {
						state = 4;
					} else {
						state = 1;
					}
				}
				if (currentStep < 5) {
					todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, (state) * sizeY, sizeX, sizeY);
				} else if (currentStep > 4 && currentStep < 10) {
					todraw = Bitmap.createBitmap(bitmap, currentStep * sizeX, state * sizeY, sizeX, sizeY);
				}
				break;
			default:
				return;
		}
		canvas.drawBitmap(todraw, targetX, targetY, paint);

	}

	@Override
	public void update(long gameTime) {
		currentFrame++;
		if (currentFrame % 7 == 0) {
			currentStep++;
		}

	}

	public void register(TutorialOverlay overlay) {
		overlay.addSurfaceObject(this);
	}
}