package work.kgeun.intelligenttutoringsystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.Header;
import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.activity.LessonsActivity;
import work.kgeun.intelligenttutoringsystem.databinding.FragmentCoursesListBinding;
import work.kgeun.intelligenttutoringsystem.items.Course;

/**
 * Created by kgeun on 2017. 11. 30..
 */

public class CoursesListFragment extends Fragment{

    FragmentCoursesListBinding binding;

    RecyclerView rvCoursesList;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    public CoursesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        //binding = DataBindingUtil.setContentView(this, R.layout.fragment_courses_list);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_courses_list, container, false);

        rvCoursesList = binding.recyclerviewCoursesList;

        rvCoursesList.setHasFixedSize(true);
        rvCoursesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ArrayList items = new ArrayList<>();

        mAdapter = new CourseAdapter(items,getActivity());
        rvCoursesList.setAdapter(mAdapter);

        final AsyncHttpClient client = new AsyncHttpClient();

        /*
        Course item = new Course("4","AAA","aaa","aaaa","10");
        items.add(item);
        mAdapter.notifyDataSetChanged();*/

        String webURL = "http://ec2-34-201-244-121.compute-1.amazonaws.com:8080/ITSWebServices/webresources/uwfitscourses";
        client.get(webURL, null, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(responseString));
                    Document doc = dBuilder.parse(is);

                    doc.getDocumentElement().normalize();

                    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                    NodeList nList = doc.getElementsByTagName("course");

                    System.out.println("----------------------------"+nList.getLength());

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;

                            String courseId = eElement.getElementsByTagName("courseId").item(0).getTextContent().trim();
                            String courseDescription = eElement.getElementsByTagName("courseDescription").item(0).getTextContent().trim();
                            String courseName = eElement.getElementsByTagName("courseName").item(0).getTextContent();
                            String difficulty = eElement.getElementsByTagName("difficulty").item(0).getTextContent();
                            String numberOfLessons = eElement.getElementsByTagName("numberOfLessons").item(0).getTextContent();

                            System.out.println("courseDescription : " + courseId);
                            System.out.println("courseId : " + courseDescription);
                            System.out.println("courseName : " + courseName);
                            System.out.println("difficulty : " + difficulty);
                            System.out.println("numberOfLessons : " + numberOfLessons);

                            Course item = new Course(courseId,courseName,courseDescription,difficulty,numberOfLessons);

                            items.add(item);

                        }
                    }

                    mAdapter.notifyDataSetChanged();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        return binding.getRoot();
    }


    class CourseAdapter extends RecyclerView.Adapter
    {

        private Context context;
        private ArrayList<Course> mItems; // Allows to remember the last item shown on screen private int lastPosition = -1;

        public CourseAdapter(ArrayList items, Context mContext) {
            context = mContext;
            mItems = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 새로운 뷰를 만든다
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course,parent,false);
            ViewHolder holder = new ViewHolder(v);
            return holder;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder vh = (ViewHolder)holder;
            vh.tvCourseName.setText(mItems.get(position).courseName);
            vh.tvCourseDescription.setText(mItems.get(position).courseDescription);
            vh.tvNumberOfLessons.setText("Scenarios : " + mItems.get(position).numberOfLessons);
            vh.tvDifficulty.setText("Difficulty : " + mItems.get(position).difficulty);
            vh.llCourseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),LessonsActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("numberOfLessons",mItems.get(position).numberOfLessons);
                    intent.putExtra("courseName",mItems.get(position).courseName);
                    intent.putExtra("courseId",mItems.get(position).courseId);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView tvCourseId;
            public TextView tvCourseName;
            public TextView tvCourseDescription;
            public TextView tvDifficulty;
            public TextView tvNumberOfLessons;
            public LinearLayout llCourseItem;

            public ViewHolder(View view)
            {
                super(view);
                tvCourseName = (TextView) view.findViewById(R.id.textview_course_name_courseitem);
                tvCourseDescription = (TextView) view.findViewById(R.id.textview_course_description_courseitem);
                tvDifficulty = (TextView) view.findViewById(R.id.textview_difficulty_courseitem);
                tvNumberOfLessons = (TextView) view.findViewById(R.id.textview_number_of_lessons_courseitem);
                llCourseItem = (LinearLayout) view.findViewById(R.id.linearlayout_course_item_background_courseitem);
            }
        }
    }
}
