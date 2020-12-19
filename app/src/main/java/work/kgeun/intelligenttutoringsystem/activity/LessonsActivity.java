package work.kgeun.intelligenttutoringsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.Header;
import work.kgeun.intelligenttutoringsystem.R;
import work.kgeun.intelligenttutoringsystem.databinding.ActivityLessonsBinding;
import work.kgeun.intelligenttutoringsystem.items.Lesson;
import work.kgeun.intelligenttutoringsystem.items.SGATQuery;
import work.kgeun.intelligenttutoringsystem.items.Scene;

/**
 * Created by kgeun on 2017. 12. 4..
 */

public class LessonsActivity extends AppCompatActivity {

    ActivityLessonsBinding binding;
    RecyclerView rvLessonsList;
    RecyclerView.Adapter mAdapter;
    public static List<Scene> allScenesList;
    public static List<SGATQuery> allQuerysList;
    public static List<SGATQuery> querysList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lessons);

        rvLessonsList = binding.recyclerviewLessonsListLessonsactivity;

        rvLessonsList.setHasFixedSize(true);
        rvLessonsList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        int position = intent.getIntExtra("itemNumber",0);
        int numberOfLessons = Integer.valueOf(intent.getStringExtra("numberOfLessons"));
        final int courseId = Integer.parseInt(intent.getStringExtra("courseId"));
        binding.textviewCourseNameLessonsactivity.setText(intent.getStringExtra("courseName"));

        final ArrayList<Lesson> allItems = new ArrayList<Lesson>();
        final ArrayList<Lesson> showingItems = new ArrayList<Lesson>();

        mAdapter = new LessonsAdapter(this,showingItems);
        rvLessonsList.setAdapter(mAdapter);

        final AsyncHttpClient client = new AsyncHttpClient();
        String webURL = "http://ec2-34-201-244-121.compute-1.amazonaws.com:8080/ITSWebServices/webresources/uwfitslessons";

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

                    NodeList nList = doc.getElementsByTagName("lesson");

                    System.out.println("----------------------------"+nList.getLength());

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;
                            /*
                            NodeList courseIdList = ((Element) nNode).getElementsByTagName("courseID");

                            Node courseIdNode = courseIdList.item(0);
                            Element courseIdElement = (Element) courseIdNode;
                            */
                            String courseId = eElement.getElementsByTagName("courseId").item(1).getTextContent();
                            String lessonId = eElement.getElementsByTagName("lessonId").item(0).getTextContent();
                            String difficulty = eElement.getElementsByTagName("difficulty").item(0).getTextContent();
                            String lessonDescription = eElement.getElementsByTagName("lessonDescription").item(0).getTextContent();
                            String lessonName = eElement.getElementsByTagName("lessonName").item(0).getTextContent();

                            Lesson item = new Lesson(courseId,lessonId,difficulty,lessonDescription,lessonName);

                            allItems.add(item);
                        }
                    }

                    for(Lesson item : allItems){
                        if(Integer.parseInt(item.getCourseId()) == courseId){
                            showingItems.add(item);
                        }
                    }

                    for(Lesson item : showingItems){
                        System.out.println("DESC : " + item.getLessonDescription());
                    }

                    mAdapter.notifyDataSetChanged();

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    class LessonsAdapter extends RecyclerView.Adapter{

        private Context context;
        private ArrayList<Lesson> mItems;

        public LessonsAdapter(Context context, ArrayList<Lesson> mItems) {
            this.context = context;
            this.mItems = mItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson,parent,false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder vh = (ViewHolder) holder;
            vh.tvLessonDescription.setText(mItems.get(position).getLessonDescription());
            vh.tvDifficulty.setText("Difficulty : " + mItems.get(position).getDifficulty());
            vh.tvLessonName.setText("Lesson Name : " + mItems.get(position).getLessonName());

            vh.llBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNewLesson(mItems.get(position).getCourseId(),mItems.get(position).getLessonId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView tvLessonName;
            public TextView tvLessonDescription;
            public TextView tvDifficulty;
            public LinearLayout llBackground;

            public ViewHolder(View view)
            {
                super(view);
                tvDifficulty = (TextView) view.findViewById(R.id.textview_difficulty_lessonitem);
                tvLessonDescription = (TextView) view.findViewById(R.id.textview_lesson_description_lessonitem);
                tvLessonName = (TextView) view.findViewById(R.id.textview_lesson_name_lessonitem);
                llBackground = (LinearLayout) view.findViewById(R.id.linearlayout_background_lessonitem);
            }
        }
    }

    private void startNewLesson(final String courseId, final String lessonId) {

        final AsyncHttpClient client = new AsyncHttpClient();
        final AsyncHttpClient client2 = new AsyncHttpClient();
        String webURL = "http://ec2-34-201-244-121.compute-1.amazonaws.com:8080/ITSWebServices/webresources/uwfitsscenes";

        client.get(webURL, null, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    allScenesList = new ArrayList<Scene>();
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(responseString));
                    Document doc = dBuilder.parse(is);

                    doc.getDocumentElement().normalize();

                    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                    NodeList nList = doc.getElementsByTagName("scene");

                    System.out.println("----------------------------"+nList.getLength());

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;

                            NodeList courseIdList = ((Element) nNode).getElementsByTagName("lessonId");
                            Node courseIdNode = courseIdList.item(0);
                            Element courseIdElement = (Element) courseIdNode;

                            String courseId = courseIdElement.getElementsByTagName("courseId").item(1).getTextContent();
                            System.out.println("courseId : " + courseId);
                            String lessonId = courseIdElement.getElementsByTagName("lessonId").item(0).getTextContent();
                            System.out.println("lessonId : " + lessonId);
                            String heartRate = eElement.getElementsByTagName("heartRate").item(0).getTextContent();
                            System.out.println("heartRate : " + heartRate);
                            String spO2 = eElement.getElementsByTagName("spO2").item(0).getTextContent();
                            String temperature = eElement.getElementsByTagName("temperature").item(0).getTextContent();
                            String airwayRespiratoryRate = eElement.getElementsByTagName("airwayRespiratoryRate").item(0).getTextContent();
                            String bloodPressureDiastolic = eElement.getElementsByTagName("bloodPressureDiastolic").item(0).getTextContent();
                            String bloodPressureSystolic = eElement.getElementsByTagName("bloodPressureSystolic").item(0).getTextContent();
                            String painLocation = eElement.getElementsByTagName("painLocation").item(0).getTextContent();
                            String orderNumber = eElement.getElementsByTagName("orderNumber").item(0).getTextContent();
                            String painLevel = eElement.getElementsByTagName("painLevel").item(0).getTextContent();
                            String sceneId = eElement.getElementsByTagName("sceneId").item(0).getTextContent();
                            String nurseQuestionsToPatient = eElement.getElementsByTagName("nurseQuestionsToPatient").item(0).getTextContent();
                            String patientAnswers = eElement.getElementsByTagName("patientAnswers").item(0).getTextContent();
                            String patientInitialDialog = eElement.getElementsByTagName("patientInitialDialog").item(0).getTextContent();
                            String time = eElement.getElementsByTagName("time").item(0).getTextContent();

                            System.out.println("orderNumber : " + orderNumber);
                            System.out.println("nurseQuestionsToPatient : " + nurseQuestionsToPatient);
                            System.out.println("patientAnswers : " + patientAnswers);
                            System.out.println("TTTIME : " + time);

                            Scene scene = new Scene();
                            scene.setCourseId(Integer.parseInt(courseId));
                            scene.setLessonId(Integer.parseInt(lessonId));
                            scene.setHeartRate(heartRate);
                            scene.setSpO2(spO2);
                            scene.setTemperature(temperature);
                            scene.setAirwayRespiratoryRate(airwayRespiratoryRate);
                            scene.setBloodPressureDiastolic(bloodPressureDiastolic);
                            scene.setBloodPressureSystolic(bloodPressureSystolic);
                            scene.setPainLocation(painLocation);
                            scene.setOrderNumber(Integer.parseInt(orderNumber));
                            scene.setPainLevel(Integer.parseInt(painLevel));
                            scene.setSceneId(Integer.parseInt(sceneId));
                            scene.setNurseQuestionsToPatient(nurseQuestionsToPatient);
                            scene.setPatientAnswers(patientAnswers);
                            scene.setPatientInitialDialog(patientInitialDialog);
                            scene.setTime(time);

                            scene.setNurseQuestionsToPatientArray(scene.getNurseQuestionsToPatient().split("~"));
                            scene.setPatientAnswersArray(scene.getPatientAnswers().split("~"));

                            allScenesList.add(scene);
                        }
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                }


                String webURL2 = "http://ec2-34-201-244-121.compute-1.amazonaws.com:8080/ITSWebServices/webresources/uwfitssagatqueries";

                client2.get(webURL2, null, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            querysList = new ArrayList<SGATQuery>();
                            allQuerysList = new ArrayList<SGATQuery>();

                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            InputSource is = new InputSource(new StringReader(responseString));
                            Document doc = dBuilder.parse(is);

                            doc.getDocumentElement().normalize();

                            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                            NodeList nList = doc.getElementsByTagName("sagatQueries");

                            System.out.println("----------------------------"+nList.getLength());

                            for (int temp = 0; temp < nList.getLength(); temp++) {

                                Node nNode = nList.item(temp);

                                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element eElement = (Element) nNode;

                                    NodeList sceneIdList = ((Element) nNode).getElementsByTagName("sceneId");
                                    Node sceneIdNode = sceneIdList.item(0);
                                    Element sceneIdElement = (Element) sceneIdNode;

                                    String sceneId = sceneIdElement.getElementsByTagName("sceneId").item(0).getTextContent();
                                    System.out.println("JIN sceneId : " + sceneId );

                                    NodeList courseIdList = sceneIdElement.getElementsByTagName("lessonId");
                                    Node courseIdNode = courseIdList.item(0);
                                    Element courseIdElement = (Element) courseIdNode;
                                    String courseId = courseIdElement.getElementsByTagName("courseId").item(1).getTextContent();
                                    System.out.println("JIN courseId : " + courseId);
                                    String lessonId = courseIdElement.getElementsByTagName("lessonId").item(0).getTextContent();
                                    System.out.println("JIN lessonId : " + lessonId);

                                    String choices = eElement.getElementsByTagName("choices").item(0).getTextContent();
                                    String correctAnswer = eElement.getElementsByTagName("correctAnswer").item(0).getTextContent();
                                    String queryId = eElement.getElementsByTagName("queryId").item(0).getTextContent();
                                    System.out.println("JIN queryId : " + queryId);
                                    String question = eElement.getElementsByTagName("question").item(0).getTextContent();
                                    System.out.println("JIN question : " + question);
                                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                                    String level = eElement.getElementsByTagName("level").item(0).getTextContent();

                                    SGATQuery query = new SGATQuery();

                                    query.setCourseId(Integer.parseInt(courseId));
                                    query.setLessonId(Integer.parseInt(lessonId));
                                    query.setSceneId(Integer.parseInt(sceneId));
                                    query.setChoices(choices);
                                    query.setCorrectAnswer(correctAnswer);
                                    query.setQueryId(Integer.parseInt(queryId));
                                    query.setQuestion(question);
                                    query.setType(Integer.parseInt(type));
                                    query.setLevel(Integer.parseInt(level));

                                    allQuerysList.add(query);

                                }
                            }

                            for(SGATQuery item : allQuerysList){
                                if(item.getCourseId() == Integer.parseInt(courseId) &&
                                        item.getLessonId() == Integer.parseInt(lessonId)){
                                    System.out.println("쿼리리스트에 아이템 : CID : " + item.getCourseId() + " LID : " + item.getLessonId());
                                    querysList.add(item);
                                }
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LessonsActivity.this,SceneActivity.class);
                        intent.putExtra("courseId",courseId);
                        intent.putExtra("lessonId",lessonId);
                        intent.putExtra("orderNumber",1);

                        startActivity(intent);
                    }
                });


            }
        });



    }
}
