package callete.test;

import callete.api.Callete;
import callete.api.services.resources.ArtistResources;
import callete.api.services.resources.ImageResource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * In the beginning, there was main...
 */
public class ArtistResourceFXExample extends Application {
  public static final int WIDTH = 700;
  public static final int HEIGHT= 345;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage primaryStage) throws Exception {
    //create root component with background
    StackPane rootStack = new StackPane();

    ArtistResources imageResourcesFor = Callete.getResourcesService().getImageResourcesFor("Adele");
    ImageResource image = imageResourcesFor.getRandomImage(700, 354, 500);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image.getImage(), image.getImageFormat(), os);
    InputStream is = new ByteArrayInputStream(os.toByteArray());
    Image fxImage = new Image(is, image.getImage().getWidth(), image.getImage().getHeight(), false, true);

    rootStack.getChildren().add(new ImageView(fxImage));

    Scene scene = new Scene(rootStack, (double) WIDTH, (double) HEIGHT);
    primaryStage.setScene(scene);
    primaryStage.getScene().setRoot(rootStack);

    primaryStage.show();
  }
}
