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
import com.tognyp.projekt.model.CountyCity;

@Controller
@RequestMapping("/powiatLubMiasto")
public class CountyCityController {
	
	@GetMapping("/powiatMiasto")
	public String insertStreetOrCity(Model theModel) {
		
		String city = "Miasto";
		String county = "Powiat";
		CountyCity countyCity = new CountyCity();
		countyCity.setType(city);
		
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
		
    
		theModel.addAttribute("city", city);
		theModel.addAttribute("county", county);
		theModel.addAttribute("countyCity", countyCity);
		theModel.addAttribute("districts", districts);
		
		return "insertCountyOrCity";
	}
	
	@PostMapping("/showZip")
	public String showZip(@ModelAttribute("countyCity") CountyCity countyCity, Model theModel) {
		
		List<Address> addressList = new ArrayList<>();
		
		Element doc = null;
		
        try{
        	switch(countyCity.getDistrict()){
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
        
        System.out.println("type: " + countyCity.getType());
        switch(countyCity.getType()) {
        	case "Miasto":
        		for(Element e : rows) {
                	Elements td = e.getElementsByTag("td");
                	if(td.get(2).text().equalsIgnoreCase(countyCity.getName().trim())) {
                		Address tmpAddress = new Address();
                		tmpAddress.setZipCode(td.get(0).text());
                		tmpAddress.setStreet(td.get(1).text());
                		tmpAddress.setCity(td.get(2).text());
                		tmpAddress.setVoivodeship(td.get(3).text());
                		tmpAddress.setCounty(td.get(4).text());
                		addressList.add(tmpAddress);
                	}
                }
        		break;
        	case "Powiat":
        		for(Element e : rows) {
                	Elements td = e.getElementsByTag("td");
                	if(td.get(4).text().equalsIgnoreCase(countyCity.getName().trim())) {
                		Address tmpAddress = new Address();
                		tmpAddress.setZipCode(td.get(0).text());
                		tmpAddress.setStreet(td.get(1).text());
                		tmpAddress.setCity(td.get(2).text());
                		tmpAddress.setVoivodeship(td.get(3).text());
                		tmpAddress.setCounty(td.get(4).text());
                		addressList.add(tmpAddress);
                	}
                }
        		break;
        }

		if(addressList.isEmpty()) {
			return "noResult";
		}
		theModel.addAttribute("addressList", addressList);
		
		return "showAddress";
	}
}
