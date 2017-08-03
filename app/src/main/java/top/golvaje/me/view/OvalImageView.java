package top.golvaje.me.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class OvalImageView extends ImageView {

	public OvalImageView(Context ctx, AttributeSet attrs) {
	    super(ctx, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
        Bitmap b;
        b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();
		int r = Math.min(w, h);
		Bitmap roundBitmap = getOvalCroppedBitmap(bitmap, r);
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}

	public static Bitmap getOvalCroppedBitmap(Bitmap bitmap, int radius) {
		Bitmap finalBitmap;
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
			finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
					false);
		else
			finalBitmap = bitmap;
		Bitmap output = Bitmap.createBitmap(radius,
				radius, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),finalBitmap.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		//RectF oval = new RectF(0, 0, 130,150);
		RectF oval = new RectF(0, 0, radius,radius);
		canvas.drawOval(oval, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(finalBitmap, rect, oval, paint);

		return output;
	}

}