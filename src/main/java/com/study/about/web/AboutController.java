package com.study.about.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutController {

		@RequestMapping("/about/aboutHome.wow")
		public String aboutHome() {
			return "about/aboutHome";
		}
		@RequestMapping("/about/changhee.wow")
		public String aboutChanghee() {
			return "about/changhee";
		}
		@RequestMapping("/about/ohtani.wow")
		public String ohtani() {
			return "about/ohtani";
		}
		@RequestMapping("/about/zimerman.wow")
		public String zimerman() {
			return "about/zimerman";
		}
}
