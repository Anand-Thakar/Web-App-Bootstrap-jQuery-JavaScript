$(document).ready(function checking(){
    $("#btn1").click(function () {
       
        let f = false;
        
        // firstname
   
        let uemail= $("#eID").val();
        console.log(uemail);

        f=false;
        let name= $("#fName").val();
        console.log(name);
        if(name==""||name==undefined){
         $("#firstnameHelp")
         .html("Name is required")
         .addClass("text-danger");
         f=false;
        } else{
         $("#firstnameHelp")
         .html("Ok!!")
         .removeClass("text-danger")
         .addClass("text-success");
         f=true;
         
        }
  

        // last name

    
        let last= $("#lName").val();
        console.log(last);
        if(last==""||last==undefined){
            $("#lastnameHelp")
            .html("Name is required")
            .addClass("text-danger");
                f=false;
           } else{
            $("#lastnameHelp")
            .html("Ok!!")
            .removeClass("text-danger")
            .addClass("text-success");
            f=true;
            
           }
           
           //phone

        let phone = $("#numberPhone").val();
        console.log(phone);
        var re = /^\d{10}$/;

         if(re.test(phone)==false){
            $("#phoneHelp")
            
            .html("valid phone number is required, without any spaces in between, should be 10 character")
            .addClass("text-danger")
                f= false;
        }
        else{
            $("#phoneHelp")
            .html("Ok!!")
            .removeClass("text-danger")
            .addClass("text-success")

            f=true;
        }

        //email

        let eMail = $("#eID").val();
        console.log(eMail);
        var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;


         if(re.test(eMail)==false){
            $("#eHelp")
            
            .html("enter valid email, abc@example.com")
            .addClass("text-danger")
           

                f= false;
        }else{
            $("#eHelp")
            .html("Ok!!")
            .removeClass("text-danger")
            .addClass("text-success")

            f=true;
        }

    });

console.log(f);
  
});
