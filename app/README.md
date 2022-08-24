
This project is based on User Biometric authentication and by using the current location to 
fetch the weather data from open weather map Api. 

1. Project is based on MVVM Architecture with JetPack Navigation.
2. Get the current location by Fused Location Provider API.
3. Used androidx biometric library to authenticate the user.
4. Created 2 fragments to complete the task. one fragment is to authenticate and get the current location
   and pass the 2 arguments latitude and longitude to the next fragment where the weather data displays according to
   the current location.
   
5. Project flow as follow.
   First Fragment is LoginScreenFragment and it has one biometric image and one login button. 
   Initially the login button set to disabled. Login button gets enabled by pressing the biometric image and 
   the dialog appears.
   
   