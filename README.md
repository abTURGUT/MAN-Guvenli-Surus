# ![MAN_-_Guvenli_Surus_small2](https://user-images.githubusercontent.com/48166225/176228010-8af876b5-6785-48d3-9163-a76397e9eefd.png)  TRAFİK IŞIĞI TESPİTİ VE ARAÇ TAKİP MESAFESİ ÖLÇÜMÜ  ![MAN_-_Guvenli_Surus_small2](https://user-images.githubusercontent.com/48166225/176228010-8af876b5-6785-48d3-9163-a76397e9eefd.png)




Bu projede görüntü işleme tekniklerini kullanarak trafik ışığının rengi ve öndeki araç ile olan
mesafenin tespitinin yapıldığı bir mobil uygulama geliştirilmiştir. Ayrıca trafik ışığını tespit ettikten sonra aracın hangi
ışıkta geçtiği uygulama tarafından kontrol edilmektedir.

Günümüzde araç kiralama şirketlerinin müşterilerine ve kurumsal şirketlerin çalışanlarına verdiği araçların ne kadar trafik kurallarına uygun kullanıldığı belirsizdir.
Aynı zamanda bu sorunu baz alarak sürücünün hangi ışıkta geçtiğini ve öndeki
araç ile olan mesafesini belirleyerek trafikte doğabilecek sürücü kaynaklı kazaların
önüne geçmek için bu proje geliştirilmiştir. Uygulamamızda birinci olarak aracın hangi ışıkta
geçtiğini belirleyerek trafik kuralını ihlal edip etmediğinin tespitinin yapılması, ikinci
olarak ise öndeki araç mesafesini hesaplayarak iki araç arasındaki korunması gereken
takip mesafesinin kontrolü sağlanabilmektedir.

Kullandığımız araçlar aşağıda belirtilmiştir:

- Model : YoloV5
- Tflite Dönüştürücü : TensorFlow
- Etiketleme : Roboflow
- Veritabanı : Firebase

Model hazır bir görüntüde aşağıdaki gibi çalışmaktadır.


https://user-images.githubusercontent.com/48166225/176230163-9a7a0b96-3a40-4953-9f9c-ff2166138d58.MOV


Model uygulamada aşağıdaki gibi çalışmaktadır.


https://user-images.githubusercontent.com/48166225/176229988-eea8c05a-6179-4ed0-85ea-e809e6535d9d.mp4


* Uygulama apk dosyası ana klasörde bulunmaktadır. 

Uygulama kullanımı :

- Uygulamaya kurumsaldan kaydolduktan sonra mailinize sisteme giriş yapacağınız şifreniz gönderilecektir. Kendi mailiniz ve şifre ile kurumsaldan giriş yapabilirsiniz.
- Kurumsal sayfasında "Yeni Kullanıcı Ekle" butonu ile açılan sayfada bireysel için kullanıcı ekleyebilirsiniz.
- Bireysel için eklenen kullanıcının mailine gelen kod ile sisteme kaydı gerçekleştirilebilmektedir.
- Modeli çalıştırmak için bireysel sayfasında en üstte "Kamerayı Başlat" butonuna basarak gerekli sayfaya gidebilir ve modeli test edebilirsiniz.

## Üyeler 

|Proje Üyeleri|
|---|
|[Muhammed Nafiz Canıtez](https://github.com/nafizcntz)|
|[Abdullah Turğut](https://github.com/abTURGUT)|
|[Mohamed Elazab](https://github.com/elazabmohamed)|
