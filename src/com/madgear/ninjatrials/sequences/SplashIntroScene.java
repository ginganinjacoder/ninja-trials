/*
 * Ninja Trials is an old school style Android Game developed for OUYA & using
 * AndEngine. It features several minigames with simple gameplay.
 * Copyright 2013 Mad Gear Games <madgeargames@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.madgear.ninjatrials.sequences;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

import com.madgear.ninjatrials.GameScene;
import com.madgear.ninjatrials.managers.GameManager;
import com.madgear.ninjatrials.managers.ResourceManager;
import com.madgear.ninjatrials.managers.SFXManager;
import com.madgear.ninjatrials.managers.SceneManager;
import com.madgear.ninjatrials.test.TestingScene;

/**
 * Splash Intro Scene for Ninja Trials
 * 
 * 
 * Design document: section 3.1 (page 4)
 * @author Madgear Games
 *
 */

public class SplashIntroScene extends GameScene{

	private final float SCREEN_WIDTH = ResourceManager.getInstance().cameraWidth;
	private final float SCREEN_HEIGHT = ResourceManager.getInstance().cameraHeight;
	
	private float startTime;
	private float timePreLogo = 0.5f;
	private float timeLogoIn = 0.5f;
	private float timeLogoStay = 1f;
	private float timeLogoOut = 0.5f;
	private float timePostLogo = 0.5f;
	private final float exitDelay = timePreLogo+timeLogoIn+timeLogoStay+timeLogoOut+timePostLogo;// 8f;
	private SequenceEntityModifier mSeqEntMod;
	
	private Sprite sprLogoMadGear;
	private IUpdateHandler updateHandler;

	@Override
	public Scene onLoadingScreenLoadAndShown() {
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() { }

	@Override
	public void onLoadScene() {
		ResourceManager.getInstance().loadSplashIntroResources();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onShowScene() {
		SFXManager.playSound(ResourceManager.getInstance().menuLogoMadgear);

		sprLogoMadGear = new Sprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 
				ResourceManager.getInstance().splashLogo.getWidth(), 
				ResourceManager.getInstance().splashLogo.getHeight(),
				ResourceManager.getInstance().splashLogo,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		sprLogoMadGear.setAlpha(0f);
		attachChild(sprLogoMadGear);

		mSeqEntMod= new SequenceEntityModifier(
                new DelayModifier(timePreLogo),// Logo still not visible
                new ParallelEntityModifier(// Logo enters
                    new AlphaModifier(timeLogoIn, 0, 1),
                    new ScaleModifier(timeLogoIn, 0.95f, 1)
                ),
                new DelayModifier(timeLogoStay), // Logo stays
                new ParallelEntityModifier(// Logo exits
                    new AlphaModifier(timeLogoOut, 1, 0),
                    new ScaleModifier(timeLogoOut, 1, 0.95f)
                ),
                new DelayModifier(timePostLogo) // Logo not visible again
        );
		sprLogoMadGear.registerEntityModifier(mSeqEntMod);

		startTime = ResourceManager.getInstance().engine.getSecondsElapsedTotal();
		updateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
            	if (ResourceManager.getInstance().engine.getSecondsElapsedTotal()>startTime+exitDelay) {
            		unregisterUpdateHandler(updateHandler);
            		skip();
            	}            	
            }
            @Override public void reset() {}            
        };
        registerUpdateHandler(updateHandler);
	}

	@Override
	public void onHideScene() { }

	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().unloadSplashIntroResources();		
	}

	private void skip() {
        if(GameManager.DEBUG_MODE)
            SceneManager.getInstance().showScene(new TestingScene());
        else
            SceneManager.getInstance().showScene(new Intro1Scene());
	}

	@Override
    public void onPressButtonMenu() {
		skip();
    }

	@Override
	public void onPressButtonO() {
		skip();
	}

	@Override
	public void onPressButtonU() {
		skip();
	}

	@Override
	public void onPressButtonY() {
		skip();
	}

	@Override
	public void onPressButtonA() {
		skip();
	}
}