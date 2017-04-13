import android.content.Intent;
import android.content.BroadcastReceiver;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent pushIntent = new Intent(context, BootService.class);
			context.startService(pushIntent);
		}
	}

}