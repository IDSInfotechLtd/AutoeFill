package govt.opendataapp.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.webkit.WebView;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

public class WebPageToPDF {
	
	private float width;
	private float scale;
	
	public WebPageToPDF() {
		// TODO Auto-generated constructor stub
	}
	
	public void convertToPDF(WebView webView, Document pdfDocument, PdfWriter writer, Picture mPicture, Bitmap mBitmap, String fileName){
//		pdfDocument = new Document();
		pdfDocument.setPageSize(PageSize.A4);
		pdfDocument.getPageSize().getHeight();
//		try{
//			File file = new File(Environment.getExternalStorageDirectory()+"/PDF");
//			if(!file.mkdirs());
//			File newfile = new File(Environment.getExternalStorageDirectory()+"/PDF", fileName.replace(" ", "_") + ".pdf");
//			if(newfile.exists()){
//				newfile.delete();
//			}
//			writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(newfile));
//			writer.open();
//        }
//        catch(Exception ex){
//        	ex.printStackTrace();
//        }
		pdfDocument.open();
		pdfDocument.setMargins(10, 10, 10, 10);
//		mPicture = webView.capturePicture();
		mBitmap = picture2Bitmap(mPicture);
		byte[] imgByteArray = bitmap2ByteArray(mBitmap);
		scale = ((float)595/mBitmap.getWidth())*100;
		if (mBitmap.getHeight() > 1500){
			separatePages(pdfDocument, mBitmap);
		}
		else{
			if(pdfDocument.getPageSize().getWidth() < mBitmap.getWidth()){					
	        	 width = pdfDocument.getPageSize().getWidth(); 
	         }
	         else{
	        	 width = mBitmap.getWidth();
	         }
			byteArrayToPDF(imgByteArray,pdfDocument);
		}
		pdfDocument.close();
		
		writer.close();
		writer = null;
	}
	
	private Bitmap picture2Bitmap(Picture picture){
        PictureDrawable pictureDrawable = new PictureDrawable(picture);
        Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(),pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
	}
	private byte[] bitmap2ByteArray(Bitmap bmp){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	private void separatePages(Document document, Bitmap screenshot)
    {
        int reminder = screenshot.getHeight() % 1500;
        int pages = screenshot.getHeight() / 1500 + (reminder > 0 ? 1 : 0);
        int y = 0;
        int height = 1500;
        
        if(document.getPageSize().getWidth() < screenshot.getWidth()){
        	width = document.getPageSize().getWidth(); 
        }
        else{
        	width = screenshot.getWidth();
        }

        for (int i = 0; i < pages; i++)
        {
            if (i == pages - 1 && reminder > 0){
                height = screenshot.getHeight() - y;
            }
            Bitmap convertedBitmap = Bitmap.createBitmap(screenshot, 0, y, screenshot.getWidth(), height, null, false);
//            add the image
            byteArrayToPDF(bitmap2ByteArray(convertedBitmap),document);

//          increment the height counter to move to the next page
            y += 1500;
//            using (System.Drawing.Bitmap pageBitmap = screenshot.Clone(new System.Drawing.Rectangle(0, y, 1020, height), System.Drawing.Imaging.PixelFormat.DontCare))
//            {
//                //add the image
//                addImage(document, pageBitmap);
//
//                //increment the height counter to move to the next page
//                y += 1500;
//            }
        }
    }
	
	 private boolean byteArrayToPDF(byte[] byteArray, Document document){
		 try{
			 Image png = Image.getInstance(byteArray);
			 
//			 png.scalePercent(scale);
			 png.scaleToFit(width - 50, document.getPageSize().getHeight());
			 document.add(png);
			 return true;
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return false;
		 }
	 }
	 
	 

}
