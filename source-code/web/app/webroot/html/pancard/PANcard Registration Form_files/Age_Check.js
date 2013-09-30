function getAge(applicnt_typ) {
    
   //alert("Debug:: Applcant Type"+applicnt_typ); 
   //alert(eval(applicnt_typ+".PAN_DT_BRTH_MM.value"));
   
    month = (eval(applicnt_typ+".PAN_DT_BRTH_MM.value") - 1);
    date = eval(applicnt_typ+".PAN_DT_BRTH_DD.value");
    year = eval(applicnt_typ+".PAN_DT_BRTH_YYYY.value");

    //alert("Month::::  "+month+"Date::::  "+date+"Year::::   "+year);

    //if (month != parseInt(month)) { alert('Type Month of birth in digits only!'); return false; }
    // if (date != parseInt(date)) { alert('Type Date of birth in digits only!'); return false; }
    // if (year != parseInt(year)) { alert('Type Year of birth in digits only!'); return false; }
    //if (year.length < 4) { alert('Type Year of birth in full!'); return false; }

    today = new Date();
    dateStr = today.getDate();
    monthStr = today.getMonth();
    yearStr = today.getFullYear();

    theYear = yearStr - year;
    theMonth = monthStr - month;
    theDate = dateStr - date;

    var days = "";
    if (monthStr == 0 || monthStr == 2 || monthStr == 4 || monthStr == 6 || monthStr == 7 || monthStr == 9 || monthStr == 11) days = 31;
    if (monthStr == 3 || monthStr == 5 || monthStr == 8 || monthStr == 10) days = 30;
    if (monthStr == 1) days = 28;

    var total_year = theYear;
    var total_month="";
    var total_day="";

    if (month < monthStr && date > dateStr) { total_year = parseInt(total_year) + 1;
	total_month = theMonth - 1; }
	if (month < monthStr && date <= dateStr) { total_month = theMonth; }
	else if (month == monthStr && (date < dateStr || date == dateStr)) { total_month = 0; }
	else if (month == monthStr && date > dateStr) { total_month = 11; }
	else if (month > monthStr && date <= dateStr) { total_year = total_year - 1;
	    total_month = ((12 - -(theMonth)) + 1); }
	else if (month > monthStr && date > dateStr) { total_month = ((12 - -(theMonth))); }

	if (date < dateStr) { total_day = theDate; }
	else if (date == dateStr) { total_day = 0; }
	else { total_year = total_year - 1; total_day = days - (-(theDate)); }

	//if (year.length < 4) { 
	//    return true;
	//}
	//alert("  Total Year  "+total_year);
	//alert("  TotalMonth   "+total_month);
	//alert("  Total day   "+total_day);
	if(year.length == 4){
	    if(total_year >75){
		var yes = confirm("The age mentioned by you in the online application is more than 75 years. Do you still want to continue?");
		if(!yes){
		    eval(applicnt_typ+".PAN_DT_BRTH_YYYY.focus()");
            return false;  
		}
     }
	  if(total_year == 75 && (total_month >= 0 || total_day >= 0)){
		var yes = confirm("The age mentioned by you in the online application is more than 75 years. Do you still want to continue?");
		if(!yes){
		    eval(applicnt_typ+".PAN_DT_BRTH_YYYY.focus()");
            return false;  
		}

	  }
	}
	return true;
}

