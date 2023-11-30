# Welcome to Benezinho 🔍🤓👍🏽 Face Detection with OpenCV: 4.7.0

The field of Computer Vision has grown a lot in recent years and the OpenCV library is one of the most widely used. In this project I demonstrate how to do facial recognition with OpenCV using only free software.
### Introduction

OpenCV, originally developed by Intel in 2000, is a multiplatform library, completely free for academic and commercial use, for the development of applications in the field of Computer Vision, simply by following the BSD Intel license model.

### Examples

#### Before:


 

![img_2.png](img%2Fin%2Fimg_2.png)


#### After:


![haarcascade_frontalface_default_xml_270596b0-5434-4bbf-9995-2d2e74b60dff.png](img%2Fout%2Fhaarcascade_frontalface_default_xml_270596b0-5434-4bbf-9995-2d2e74b60dff.png)


 

### Directories

1. img/in : Directory containing the images to be processed

   2. img/in/outros : The images in this directory will not be processed.

2. img/out : Directory where the images are saved after facial recognition. The name of the generated file begins with the name of the classifier followed by a UUID.

3. data : This directory contains the OpenCV files used for recognition.

4. data/faces : The files that the code is using for face recognition. By default I'm using two classifier files:
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

