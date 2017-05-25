package pkgDatabase.pkgListener;

import android.graphics.Bitmap;

public interface OnQRCodeGeneratedListener {
    void generateQRCodeFinished(Bitmap qrCode);
}
