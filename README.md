## Senviron <br/>
<p align="right">
<b>A Prototype of Multimodal Atopic Dermatitis Precaution System</b>
</p>

### Background <br/>
Atopic Dermatitis (AD) is a chronic skin disease directly caused by dysfunction of skin barrier(Stratum Corneum). Being well-known as a prevalent disease among children, AD is a global issue of reducing the quality of lives of both children and their parents. In Singapore, about 20% school-going children are suffering from AD.(1)
<p align="center"> 
  <img src="img/symptom.png">
</p>
The most common soothing method is to apply lotion.<br/>
<b>However the issue is when to apply lotion and here comes the aim of my project<b/><br/>


### Aim and Detail

This is the Android APP designed for Environment Sensor (Plantower PMS5003).<br/>
The device is built on Bluno Beetle.(MCU: ATmega328P, BLE: TI CC2540).<br/>
The APP is built based on sample 

### Hardware Setup

Arduino code could be found below. 

```C
char col;
unsigned int PMSa = 0,FMHDSa = 0,TPSa = 0,HDSa = 0,PMSb = 0,FMHDSb = 0,TPSb = 0,HDSb = 0;
unsigned int PMS = 0,FMHDS = 0,TPS = 0,HDS = 0,CR1 = 0,CR2 = 0;
unsigned char buffer_RTT[40]={};   //Serial buffer; Received Data
char tempStr[15];
void setup() 
{
  Serial.begin(115200);
  Serial1.begin(9600);
}

void loop() 
{
  while(!Serial1.available());
  while(Serial1.available()>0)   //Data check: weather there is any Data in Serial1
  {
    for(int i=0;i<40;i++)
    {
      col =Serial1.read();
      buffer_RTT[i]=(char)col;
      delay(2);
    }

    Serial1.flush();

    CR1 =(buffer_RTT[38]<<8) + buffer_RTT[39];
    CR2 = 0;
    for(int i=0;i<38;i++)
      CR2 += buffer_RTT[i];
    if(CR1 == CR2)                //Check
    {
      PMSa=buffer_RTT[12];         //Read PM2.5 High 8-bit
      PMSb=buffer_RTT[13];         //Read PM2.5 Low 8-bit
      PMS=(PMSa<<8)+PMSb;          //PM2.5 value
      TPSa=buffer_RTT[30];          //Read Temperature High 8-bit
      TPSb=buffer_RTT[31];          //Read Temperature Low 8-bit
      TPS=(TPSa<<8)+TPSb;        //Temperature value
      HDSa=buffer_RTT[32];          //Read Humidity High 8-bit
      HDSb=buffer_RTT[33];          //Read Humidity Low 8-bit
      HDS=(HDSa<<8)+HDSb;      //Humidity value
    }
    else
    {
      PMS = 0;
      FMHDS = 0;
      TPS = 0;
      HDS = 0;
    }
  }

  Serial.print("Temp : ");
  sprintf(tempStr,"%d%d.%d",TPS/100,(TPS/10)%10,TPS%10);
  Serial.print(tempStr);  
  Serial.println("C");               //Serial pring Temperature
  Serial.print("RH   : "); 
  sprintf(tempStr,"%d%d.%d",HDS/100,(HDS/10)%10,HDS%10);              
  Serial.print(tempStr);            //Serial print humidity
  Serial.println(" %");               //"%"
  Serial.print("PM2.5: ");
  Serial.print(PMS);            
  Serial.println(" ug/m3");       // Serial print PM2.5, unit: ug/m³
  Serial.println(); 
}
```
### APP on Android Platform
![demo](img/demo.png) <br/>
### SVM Model 


### Task List

### Reference
