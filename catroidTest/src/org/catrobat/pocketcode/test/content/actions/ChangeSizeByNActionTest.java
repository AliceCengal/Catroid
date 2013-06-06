/**
 *  Pocket Code: An on-device visual programming system for Android devices
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
package org.catrobat.pocketcode.test.content.actions;

import java.io.File;

import org.catrobat.pocketcode.ProjectManager;
import org.catrobat.pocketcode.common.Constants;
import org.catrobat.pocketcode.content.Project;
import org.catrobat.pocketcode.content.Sprite;
import org.catrobat.pocketcode.content.actions.ChangeSizeByNAction;
import org.catrobat.pocketcode.content.actions.ExtendedActions;
import org.catrobat.pocketcode.formulaeditor.Formula;
import org.catrobat.pocketcode.io.StorageHandler;
import org.catrobat.pocketcode.test.R;
import org.catrobat.pocketcode.test.utils.TestUtils;
import org.catrobat.pocketcode.utils.UtilFile;

import android.test.InstrumentationTestCase;

public class ChangeSizeByNActionTest extends InstrumentationTestCase {

	private static final int POSITIVE_SIZE = 20;

	private Formula positiveSize = new Formula(POSITIVE_SIZE);
	//	private float negativeSize = -30;

	private static final int IMAGE_FILE_ID = R.raw.icon;

	private File testImage;
	private final String projectName = "testProject";

	@Override
	protected void setUp() throws Exception {

		File projectFile = new File(Constants.DEFAULT_ROOT + "/" + projectName);

		if (projectFile.exists()) {
			UtilFile.deleteDirectory(projectFile);
		}

		Project project = new Project(getInstrumentation().getTargetContext(), projectName);
		StorageHandler.getInstance().saveProject(project);
		ProjectManager.getInstance().setProject(project);

		testImage = TestUtils.saveFileToProject(this.projectName, "testImage.png", IMAGE_FILE_ID, getInstrumentation()
				.getContext(), TestUtils.TYPE_IMAGE_FILE);
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
		assertEquals("Unexpected initial sprite size value", 1f, sprite.look.getSize());

		float initialSize = sprite.look.getSize();

		ChangeSizeByNAction action = ExtendedActions.changeSizeByN(sprite, positiveSize);
		sprite.look.addAction(action);
		action.act(1.0f);
		assertEquals("Incorrect sprite size value after ChangeSizeByNBrick executed", initialSize + POSITIVE_SIZE
				/ 100f, sprite.look.getSize());

	}

	public void testNullSprite() {
		ChangeSizeByNAction action = ExtendedActions.changeSizeByN(null, positiveSize);
		try {
			action.act(1.0f);
			fail("Execution of ChangeSizeByNBrick with null Sprite did not cause a NullPointerException to be thrown");
		} catch (NullPointerException e) {
			// expected behavior
		}
	}

}
