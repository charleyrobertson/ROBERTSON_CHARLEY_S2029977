package org.me.gcu.robertson_charley_s2029977.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.me.gcu.robertson_charley_s2029977.R;
import org.me.gcu.robertson_charley_s2029977.helpers.DurationHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

//Name: Charley Robertson - Student ID: S2029977
public class ListAdapter extends ArrayAdapter<TrafficItem>  implements OnClickListener {

    private Context c;
    private List<TrafficItem> list;
    public TrafficItem item;
    SimpleDateFormat formatDateNew =  new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    DurationHelper durationHelper = new DurationHelper();

    public ListAdapter(Context contextIn, List<TrafficItem> listIn) {
        super(contextIn, R.layout.card_view, listIn);
        this.c = contextIn;
        this.list = listIn;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getApplicationContext().getSystemService(c.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card_view, parent, false);
        TextView title = card.findViewById(R.id.cardview_title);
        TextView desc = card.findViewById(R.id.short_description1);
        TextView cLink = card.findViewById(R.id.link);
        ImageView cardImage = card.findViewById(R.id.cardview_image);
        TextView durationTxt = card.findViewById(R.id.duration);

        cLink.setOnClickListener(view -> { onClick(view); });

        item = list.get(pos);

        //Duration set up
        long duration = item.getDuration();
        if(duration ==  0) { duration = 1; }

        if(list.get(pos).getType() == TrafficItemType.Roadworks)
        {
            cardImage.setImageResource(R.drawable.ic_baseline_traffic_24);

            String startDate = formatDateNew.format(list.get(pos).getStartDate());
            String endDate = formatDateNew.format(list.get(pos).getEndDate());

            desc.setText(new StringBuilder().append("Start Date: ").append(startDate).append("\n").append("End Date: ").append(endDate));

            durationTxt.setText(new StringBuilder().append("Duration: ").append(durationHelper.totalDuration(duration)).toString());
            durationTxt.setTextColor(durationHelper.findColor(duration));
        }
        else if(list.get(pos).getType() == TrafficItemType.PlannedRoadworks)
        {
            cardImage.setImageResource(R.drawable.ic_baseline_edit_road_24);

            String startDate = formatDateNew.format(list.get(pos).getStartDate());
            String endDate = formatDateNew.format(list.get(pos).getEndDate());

            desc.setText(new StringBuilder().append("Start Date: ").append(startDate).append("\n").append("End Date: ").append(endDate));

            durationTxt.setText(new StringBuilder().append("Duration: ").append(durationHelper.totalDuration(duration)).toString());
            durationTxt.setTextColor(durationHelper.findColor(duration));
        }
        else if(list.get(pos).getType() == TrafficItemType.Incidents)
        {
            cardImage.setImageResource(R.drawable.ic_baseline_commute_24);
            desc.setText(list.get(pos).getDescription());
        }

        title.setText(list.get(pos).getTitle());

        return card;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("More Information");
        alertDialog.setMessage("Description: " + item.getDescription() +
                "Link: " + item.getLink() + "\n"
                + "Publish Date: " + item.getPubDate().toString());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
