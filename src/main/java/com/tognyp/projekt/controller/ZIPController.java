package com.tognyp.projekt.controller;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tognyp.projekt.model.Address;
import com.tognyp.projekt.model.ZipCode;

@Controller
@RequestMapping("/kod")
public class ZIPController {

	@GetMapping("/podajKod")
	public String insertZipCode(Model theModel) {
		
		/*
		Element doc = null;
        try{
            doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/").get();
        }catch (Exception e){
        	System.out.println("*Brak połączenia ze stroną*");
        }
        Element table = doc.getElementById("page-table");
        Elements rows = table.getElementsByTag("tr");
        
        System.out.println(rows);
		*/
		
		
        ZipCode zipCode = new ZipCode();
        
        List<String> districts = new ArrayList<>();
        districts.add("okręg warszawski (0x-xxx)");
        districts.add("okręg olsztyński (1x-xxx)");
        districts.add("okręg lubelski (2x-xxx)");
        districts.add("okręg krakowski (3x-xxx)");
        districts.add("okręg katowicki (4x-xxx)");
        districts.add("okręg wrocławski (5x-xxx)");
        districts.add("okręg poznański (6x-xxx)");
        districts.add("okręg szczeciński (7x-xxx)");
        districts.add("okręg gdański (8x-xxx)");
        districts.add("okręg łódzki (9x-xxx)");
        
        theModel.addAttribute("zipCode", zipCode);
		theModel.addAttribute("districts", districts);
        
		return "insertZip";
	}
	
	@PostMapping("/showAddress")
	public String showAddress(@ModelAttribute("zipCode") ZipCode zipCode, Model theModel) {
		
		List<Address> addressList = new ArrayList<>();
		
		Element doc = null;
		
		System.out.println("district with spaces: " + zipCode.getDistrict().length());
		System.out.println("district without spaces: " + zipCode.getDistrict().trim().length());
		
        try{
        	switch(zipCode.getDistrict()){
        		case "okręg warszawski (0x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/").get();
        			break;
        		case "okręg olsztyński (1x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg1.php").get();
        			break;
        		case "okręg lubelski (2x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg2.php").get();
        			break;
        		case "okręg krakowski (3x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg3.php").get();
        			break;
        		case "okręg katowicki (4x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg4.php").get();
        			break;
        		case "okręg wrocławski (5x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg5.php").get();
        			break;
        		case "okręg poznański (6x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg6.php").get();
        			break;
        		case "okręg szczeciński (7x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg7.php").get();
        			break;
        		case "okręg gdański (8x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg8.php").get();
        			break;
        		case "okręg łódzki (9x-xxx)":
        			doc = Jsoup.connect("https://www.kody-pocztowe.dokladnie.com/okreg9.php").get();
        			break;
        	}
        }catch (Exception e){
        	System.out.println("Błąd połączenia");
        }
        Element table = doc.getElementById("page-table");
        Elements rows = table.getElementsByTag("tr");
        
        for(Element e : rows) {
        	Elements td = e.getElementsByTag("td");
        	if(td.get(0).text().equals(zipCode.getZipCode().trim())) {
        		Address tmpAddress = new Address();
        		tmpAddress.setZipCode(td.get(0).text());
        		tmpAddress.setStreet(td.get(1).text());
        		tmpAddress.setCity(td.get(2).text());
        		tmpAddress.setVoivodeship(td.get(3).text());
        		tmpAddress.setCounty(td.get(4).text());
        		addressList.add(tmpAddress);
        	}
        }
		
		System.out.println("zipCode: " + zipCode.getZipCode());
		if(addressList.isEmpty()) {
			return "noResult";
		}
		theModel.addAttribute("addressList", addressList);
		
		return "showAddress";
	}
}
