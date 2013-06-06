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
package org.catrobat.pocketcode.uitest.content.brick;

import java.util.ArrayList;

import org.catrobat.pocketcode.ProjectManager;
import org.catrobat.pocketcode.R;
import org.catrobat.pocketcode.uitest.util.UiTestUtils;
import org.catrobat.pocketcode.content.Project;
import org.catrobat.pocketcode.content.Script;
import org.catrobat.pocketcode.content.Sprite;
import org.catrobat.pocketcode.content.StartScript;
import org.catrobat.pocketcode.content.bricks.Brick;
import org.catrobat.pocketcode.content.bricks.SetVolumeToBrick;
import org.catrobat.pocketcode.ui.ScriptActivity;
import org.catrobat.pocketcode.ui.adapter.BrickAdapter;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

public class SetVolumeToBrickTest extends ActivityInstrumentationTestCase2<ScriptActivity> {
	private static final float VOLUME = 50.0f;

	private Solo solo;
	private Project project;
	private SetVolumeToBrick setVolumeToBrick;

	public SetVolumeToBrickTest() {
		super(ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		createProject();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
		solo = null;
	}

	@Smoke
	public void testSetVolumeToBrick() {
		ListView dragDropListView = UiTestUtils.getScriptListView(solo);
		BrickAdapter adapter = (BrickAdapter) dragDropListView.getAdapter();

		int childrenCount = adapter.getChildCountFromLastGroup();
		int groupCount = adapter.getScriptCount();

		assertEquals("Incorrect number of bricks.", 2, dragDropListView.getChildCount());
		assertEquals("Incorrect number of bricks.", 1, childrenCount);

		ArrayList<Brick> projectBrickList = project.getSpriteList().get(0).getScript(0).getBrickList();
		assertEquals("Incorrect number of bricks.", 1, projectBrickList.size());

		assertEquals("Wrong Brick instance.", projectBrickList.get(0), adapter.getChild(groupCount - 1, 0));
		assertNotNull("TextView does not exist.", solo.getText(solo.getString(R.string.brick_set_volume_to)));

		UiTestUtils.testBrickWithFormulaEditor(solo, 0, 1, VOLUME, "volume", setVolumeToBrick);
	}

	private void createProject() {
		project = new Project(null, UiTestUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite sprite = new Sprite("cat");
		Script script = new StartScript(sprite);
		setVolumeToBrick = new SetVolumeToBrick(sprite, 0);
		script.addBrick(setVolumeToBrick);

		sprite.addScript(script);
		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}
}
