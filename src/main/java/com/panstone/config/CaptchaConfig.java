package com.panstone.config;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.image.ImageFilter;

@Configuration
public class CaptchaConfig {

	@Bean
	public ImageCaptchaService imageCaptchaService() {
		CaptchaEngine captchaEngine = new CustomCaptchaEngine();
		return new GenericManageableCaptchaService(captchaEngine, 100, 180000, 75000);
	}

	static class CustomCaptchaEngine extends ListImageCaptchaEngine {
		@Override
		protected void buildInitialFactories() {
			int minWordLength = 5;
			int maxWordLength = 6;
			int fontSize = 30;
			int imageWidth = 200;
			int imageHeight = 50;

			// word generator
			WordGenerator dictionaryWords = new ComposeDictionaryWordGenerator(new FileDictionary("toddlist"));

			// word2Image components
			ColorGenerator colorGen = new RandomListColorGenerator(
					new Color[] {
							new Color(23, 170, 27),
							new Color(220, 34, 11),
							new Color(23, 67, 172)
					}
			);

			TextPaster randomPaster = new DecoratedRandomTextPaster(minWordLength, maxWordLength, colorGen, new TextDecorator[] {});
			BackgroundGenerator background = new UniColorBackgroundGenerator(imageWidth, imageHeight, Color.WHITE);
			FontGenerator font = new RandomFontGenerator(fontSize, fontSize,
					new Font[] {
							new Font("nyala", Font.BOLD, fontSize),
							new Font("Bell MT", Font.PLAIN, fontSize),
							new Font("Credit valley", Font.BOLD, fontSize)
					});

			ImageDeformation backDef = new ImageDeformationByFilters(new ImageFilter[] {});
			ImageDeformation textDef = new ImageDeformationByFilters(new ImageFilter[] {});
			ImageDeformation postDef = new ImageDeformationByFilters(new ImageFilter[] {});

			WordToImage word2Image = new DeformedComposedWordToImage(font, background, randomPaster, backDef, textDef, postDef);
			addFactory(new GimpyFactory(dictionaryWords, word2Image));
		}
	}

}
