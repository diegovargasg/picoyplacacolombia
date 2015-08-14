package com.diegodevelopero.PicoyPlacaReminder;

import java.util.Calendar;

public class Car {
	
	private int id;
	private int placa;
	
	/**
	 * Class Construct
	 * @param arg_icon
	 * @param arg_placa
	 */
	public Car(int arg_icon, int arg_placa){
		this.id = arg_icon;
		this.placa = arg_placa;
	}
	
	/**
	 * get the resource based on the icon of the car
	 * @return integer
	 */
	public int getCarDraw(){
		
		int rsc = 0;
		
		switch (this.id) {
		case 0:
			rsc = R.drawable.car_blue;
			break;
		case 1:
			rsc = R.drawable.car_red;
			break;
		case 2:
			rsc = R.drawable.car_green;
			break;
		case 3:
			rsc = R.drawable.car_yellow;
			break;
		case 4:
			rsc = R.drawable.car_black;
			break;
		case 5:
			rsc = R.drawable.car_white;
			break;
		case 6:
			rsc = R.drawable.truck_black;
			break;
		case 7:
			rsc = R.drawable.truck_white;
			break;
		case 8:
			rsc = R.drawable.truck_blue;
			break;
		case 9:
			rsc = R.drawable.truck_red;
			break;
		case 10:
			rsc = R.drawable.truck_green;
			break;
		default:
			rsc = R.drawable.car_blue;
			break;
		}
		return rsc;
	}
	
	/**
	 * Return boolean depending if the car has or not pico y placa
	 * @return boolean
	 */
	public boolean hasPico(String number){
		
		String[] temp;
		boolean found = false;
		
		if(!number.equals("null")){
			temp = number.split("-");
			for(int i = 0; i < temp.length ; i++){
				if(Integer.parseInt(temp[i]) == this.placa){
					found = true;
					break;
				}
			}
		}
		return found;
	}
	
	public boolean hasPicoBogota(int placa){
		Calendar calendar;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
				
		if(currentDay%2 == 0 && (placa%2) == 0){// day is Even
			return true;
		}else if(currentDay%2 != 0 && (placa%2) != 0){//Odd
			return true;
		}else{
			return false;
		}
	}
	
	public String picoyplacaTodayParse(String number){
		
		String finalString = "";
		
		if(!number.equals("null")){
			String[] temp;
			temp = number.split("-");
			
			for(int i = 0; i < temp.length ; i++){
				finalString = finalString+temp[i];
			}
		}
		
		return finalString;
	}
}
