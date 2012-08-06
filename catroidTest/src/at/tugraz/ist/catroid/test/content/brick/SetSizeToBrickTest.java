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
package at.tugraz.ist.catroid.test.content.brick;

import java.io.File;

import android.content.Context;
import android.test.InstrumentationTestCase;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.common.Constants;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.bricks.SetSizeToBrick;
import at.tugraz.ist.catroid.test.R;
import at.tugraz.ist.catroid.test.utils.TestUtils;
import at.tugraz.ist.catroid.utils.UtilFile;
import at.tugraz.ist.catroid.utils.Utils;

public class SetSizeToBrickTest extends InstrumentationTestCase {
	private static final int IMAGE_FILE_ID = R.raw.icon;
	private static final String projectName = "testProject";

	private Context context;
	private File testImage;
	private double size = 70;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getInstrumentation().getTargetContext();

		File projectFile = new File(Constants.DEFAULT_ROOT + "/" + projectName);

		if (projectFile.exists()) {
			UtilFile.deleteDirectory(projectFile);
		}

		Project project = new Project(context, projectName);
		assertTrue("cannot save project", TestUtils.saveProjectAndWait(this, project));
		ProjectManager.getInstance().setProject(project);

		testImage = TestUtils.saveFileToProject(projectName, "testImage.png", IMAGE_FILE_ID, context,
				TestUtils.TYPE_IMAGE_FILE);

		Utils.updateScreenWidthAndHeight(context);
	}

	@Override
	protected void tearDown() throws Exception {
		File projectFile = new File(Constants.DEFAULT_ROOT + "/" + projectName);

		if (projectFile.exists()) {
			UtilFile.deleteDirectory(projectFile);
		}
		if (testImage != null && testImage.exists()) {
			testImage.delete();
		}
		super.tearDown();
	}

	public void testSize() {
		Sprite sprite = new Sprite("testSprite");
		assertEquals("Unexpected initial sprite size value", 1f, sprite.costume.scaleX);
		assertEquals("Unexpected initial sprite size value", 1f, sprite.costume.scaleY);

		SetSizeToBrick setSizeToBrick = new SetSizeToBrick(sprite, size);
		setSizeToBrick.execute();
		assertEquals("Incorrect sprite size value after SetSizeToBrick executed", (float) size / 100,
				sprite.costume.scaleX);
		assertEquals("Incorrect sprite size value after SetSizeToBrick executed", (float) size / 100,
				sprite.costume.scaleY);
	}

	public void testNullSprite() {
		SetSizeToBrick setSizeToBrick = new SetSizeToBrick(null, size);

		try {
			setSizeToBrick.execute();
			fail("Execution of SetSizeToBrick with null Sprite did not cause a NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

}
