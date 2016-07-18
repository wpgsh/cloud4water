
//对空字段进行去空处理
function trimStr(trimValue){

	if(undefined == trimValue || null == trimValue || "" == trimValue){
		return "";
	}else{
		return trimValue;
	}
}

//对空字段进行空空判断
function isEmp(str){
	if(undefined == str || null == str || "" == str){
		return true;
	}
	str = str.trim();
	if(undefined == str || null == str || "" == str){
		return true;
	}
	return false;
}

//将form数据序列化
serializeObject=function(serializeObj){
	
	var obj=new Object();
	$.each(serializeObj.serializeArray(),function(index,param){
	
	if(!(param.name in obj)){
		obj[param.name]=param.value;
	}
});
return obj;
}

//将序列化数据转成JSON
toJSON = function(serValue){
	 return JSON.stringify(serValue);
}