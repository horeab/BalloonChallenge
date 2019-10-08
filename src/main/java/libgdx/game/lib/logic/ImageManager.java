package libgdx.game.lib.logic;

import main.balloonchallenge.R;
import util.AutoResizeTextView;
import util.MatrixValue;
import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageManager {

	private Activity context;

	public ImageManager(Activity context) {
		this.context = context;
	}

	public ImageView getImage(MatrixValue matrixValue) {
		return createImgView(matrixValue);
	}

	public RelativeLayout getFinalPositionImageWithPoints(int points, int imageViewId, MatrixValue finalPlayerValue) {

		RelativeLayout container = new RelativeLayout(context);
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		container.setLayoutParams(containerParams);

		ImageView imageView = createImgView(finalPlayerValue);
		RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		imageViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(imageViewParams);
		imageView.setId(imageViewId);

		AutoResizeTextView textView = (AutoResizeTextView) context.getLayoutInflater().inflate(
				context.getResources().getIdentifier("templatetxtv", "layout", context.getPackageName()), null);
		textView.setText(points + "");
		int textColor = R.color.white;
		if (finalPlayerValue == MatrixValue.FINAL_PLAYER_2) {
			textColor = R.color.black;
		}
		textView.setTextColor(context.getResources().getColor(textColor));
		textView.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, imageViewId);
		params.addRule(RelativeLayout.ALIGN_LEFT, imageViewId);
		params.addRule(RelativeLayout.ALIGN_RIGHT, imageViewId);
		params.addRule(RelativeLayout.ALIGN_TOP, imageViewId);
		textView.setLayoutParams(params);
		textView.setPercentToReduceTextSize(40);

		container.addView(imageView);
		container.addView(textView);

		return container;
	}

	private ImageView createImgView(MatrixValue matrixValue) {
		ImageView image = new ImageView(context);
		int resID = getResourceIdForMatrixVal(matrixValue);
		image.setImageResource(resID);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setAdjustViewBounds(true);
		return image;
	}

	private int getResourceIdForMatrixVal(MatrixValue matrixValue) {
		String imageIdentifier = matrixValue.getImageName();
		int resID = context.getResources().getIdentifier(imageIdentifier, "drawable", context.getPackageName());
		return resID;
	}
}
