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