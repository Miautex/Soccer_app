package pkgDatabase;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import pkgDatabase.pkgListener.OnQRCodeGeneratedListener;

public class GenerateQRCodeTask extends AsyncTask<Object, Void, Bitmap> {
    OnQRCodeGeneratedListener listener = null;

    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap bitmap = null;
        int width=500,height=500;
        try {
            String content = String.valueOf(params[0]);
            listener = (OnQRCodeGeneratedListener) params[1];

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);//256, 256
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);//guest_pass_background_color
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute (Bitmap qrCode) {
        if (listener != null) {
            listener.generateQRCodeFinished(qrCode);
        }
    }
}
