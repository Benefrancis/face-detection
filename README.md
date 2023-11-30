# Welcome to Benezinho 🔍🤓👍🏽 Face Detection with OpenCV: 4.7.0

A área de Computer Vision cresceu muito nos últimos anos a biblioteca OpenCV é uma das mais utilizadas. Neste projeto demosntro como fazer reconhecimento facial com OpenCV utilizando apenas softwares livres.

### Introdução

OpenCV, originalmente, desenvolvida pela Intel, em 2000, é uma biblioteca multiplataforma, totalmente livre ao uso acadêmico e comercial, para o desenvolvimento de aplicativos na área de Visão computacional, bastando seguir o modelo de licença BSD Intel.

### Exemplos

#### Antes:


 

![img_2.png](img%2Fin%2Fimg_2.png)


#### Depois:


![haarcascade_frontalface_default_xml_270596b0-5434-4bbf-9995-2d2e74b60dff.png](img%2Fout%2Fhaarcascade_frontalface_default_xml_270596b0-5434-4bbf-9995-2d2e74b60dff.png)


 

### Diretórios

1. img/in  : Diretório em que estão as imagens que serão processadas
   
   2. img/in/outras  : As imagens neste diretório não serão processadas.

2. img/out : Diretório onde é salvo as imagens após o reconhecimento facial. O nome do arquivo gerado começa com o nome do classificador seguido de um UUID.

3. data    : Neste diretório ficam os arquivos do OpenCV que são utilizados para reconhecimento.

4. data/faces : Os arquivos que o código está utilizando para reconhecimento facial. Por padrão estou usando dois arquivos classificadores:
   1.  haarcascade_frontalface_default.xml
   2.  haarcascade_profileface.xml





```xml

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
        <groupId>br.com.benefrancis</groupId>
        <artifactId>face-detection</artifactId>
        <version>1.0-SNAPSHOT</version>
    
        <properties>
            <maven.compiler.source>11</maven.compiler.source>
            <maven.compiler.target>11</maven.compiler.target>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        </properties>
        
        <dependencies>
            <dependency>
                <groupId>org.openpnp</groupId>
                <artifactId>opencv</artifactId>
                <version>4.7.0-0</version>
            </dependency>
        </dependencies>
    </project>

```

### Código Fonte

````Java 
   package br.com.benefrancis;
   
   import nu.pattern.OpenCV;
   import org.opencv.core.*;
   import org.opencv.imgcodecs.Imgcodecs;
   import org.opencv.imgproc.Imgproc;
   import org.opencv.objdetect.CascadeClassifier;
   import org.opencv.objdetect.Objdetect;
   
   import java.io.File;
   import java.util.Arrays;
   import java.util.UUID;
   
   public class Main {
      //Onde fica as imagens a serem processadas
      private static final String SOURCE_IMAGE_PATH_IN = "img/in";
      //Onde as imangens processadas serão salvas
      private static final String SOURCE_IMAGE_PATH_OUT = "img/out";
      //Arquivos com o critério de processamento
      private static final String OPENCV_DATA_PATH = "data/faces";
   
      public static void main(String[] args) {
   
         OpenCV.loadShared();
   
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
   
         System.out.println( "Welcome to Benezinho 🔍🤓👍🏽 Image Detection with OpenCV: " + Core.VERSION );
   
   
   
         File entrada = new File( SOURCE_IMAGE_PATH_IN );
   
         File saida = new File( SOURCE_IMAGE_PATH_OUT );
   
         File criterios = new File( OPENCV_DATA_PATH );
   
         //Limpando os intens processados anteriormente.
         Arrays.stream( saida.listFiles() ).forEach( file -> {
            file.delete();
         } );
   
         Arrays.stream( entrada.listFiles() ).forEach( file -> {
            if (file.isFile()) {
               //Navegando por todos os critérios (classificadores) de reconhecimento do diretório
               Arrays.stream( criterios.listFiles() ).forEach( criterio -> {
   
                  //Os arquivos sempre serão xml, então:
                  if (criterio.getName().endsWith( ".xml" )) {
                     findAndSave( file, criterio );
                  }
               } );
            }
         } );
      }
   
      /**
       * @param f imagem
       * @param a critério de classificação
       */
      private static void findAndSave(File f, File a) {
         Mat loadedImage = loadImage( f.getPath() );
   
         MatOfRect facesDetected = new MatOfRect();
   
         CascadeClassifier cascadeClassifier = new CascadeClassifier();
   
         //Carregando o classificador
         cascadeClassifier.load( a.getPath() );
   
         int minFaceSize = Math.round( loadedImage.rows() * 0.000001f );
   
         //Detectando
         cascadeClassifier.detectMultiScale( loadedImage,
                 facesDetected,
                 1.1,
                 3,
                 Objdetect.CASCADE_SCALE_IMAGE,
                 new Size( minFaceSize, minFaceSize ),
                 new Size()
         );
   
         Rect[] facesArray = facesDetected.toArray();
   
         System.out.println( "DETECTED: " + facesArray.length );
   
         for (Rect face : facesArray) {
            Imgproc.rectangle( loadedImage, face.tl(), face.br(), new Scalar( 0, 0, 255 ), 1 );
         }
   
         String parent = a.getName().replace( ".", "_" );
   
         if (facesArray.length > 0)
            saveImage( loadedImage, SOURCE_IMAGE_PATH_OUT + "/" + parent + "_" + UUID.randomUUID() + ".png" );
      }
   
      public static Mat loadImage(String imagePath) {
         Imgcodecs imageCodecs = new Imgcodecs();
         return imageCodecs.imread( imagePath );
      }
   
      public static void saveImage(Mat imageMatrix, String targetPath) {
         Imgcodecs imgcodecs = new Imgcodecs();
         imgcodecs.imwrite( targetPath, imageMatrix );
      }
   }
````

