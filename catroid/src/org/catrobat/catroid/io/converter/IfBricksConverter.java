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
package org.catrobat.catroid.io.converter;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IfBricksConverter implements Converter {

	public IfBricksConverter() {
		super();
	}

	@Override
	public boolean canConvert(Class clazz) {
		boolean canConvert = IfLogicEndBrick.class == clazz;

		return canConvert;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		throw new RuntimeException("not implemented!");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		reader.moveDown();

		Sprite sprite = (Sprite) context.convertAnother(IfLogicEndBrick.class, Sprite.class);

		reader.moveUp();
		reader.moveDown();

		IfLogicBeginBrick beginBrick = (IfLogicBeginBrick) context.convertAnother(IfLogicEndBrick.class,
				IfLogicBeginBrick.class);

		reader.moveUp();
		reader.moveDown();

		IfLogicElseBrick elseBrick = (IfLogicElseBrick) context.convertAnother(IfLogicEndBrick.class,
				IfLogicElseBrick.class);

		reader.moveUp();

		IfLogicEndBrick brick = new IfLogicEndBrick(sprite, elseBrick, beginBrick);

		return brick;
	}
}
