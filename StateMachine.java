/**
 * Your application code goes here
 */
package userclasses;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ShareButton;
import com.codename1.components.WebBrowser;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.io.services.ImageDownloadService;
import com.codename1.messaging.Message;
import com.codename1.processing.Result;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.codename1.util.StringUtil;
import com.mtechcomm.mobilehook.*;
//import com.mtechcomm.mobilehook.Songs;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
//import sun.java2d.pipe.DrawImage;

/**
 *
 * @author Agada Godwin C.
 */
public class StateMachine extends StateMachineBase {

    Vector<Hashtable> allTunes;
    Vector<Hashtable> allTopTunes;
    Vector<Hashtable> allGists;
    Vector<Hashtable> allVideos;
    String status;
    Songs song;
    Gists gist;
    FAQs anFAQ;
    NewsAndLifeStyles aLife;
    String myNumber;
    String myNetwork;
    String response;
    String vibeResult;
    String sendingForm;
    int tuneBegining;
    int gistBegining;
    boolean isFinished = false;
    int pageNo;
    private String service;
    private Result result;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of the
     * constructor/class scope to avoid race conditions
     */
    @Override
    protected void initVars(Resources res) {
        NetworkManager.getInstance().addErrorListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //alert("Network Problem", "There is a problem accessing the network, please ensure that WiFi or 3G is enabled and connected");
                //Dialog.show("Network Problem", "There is a problem accessing the network, please ensure that WiFi or 3G is enabled and connected", "OK", null);
                evt.consume();
            }
        });
    }

    private void searchTune(String text) {
        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                JSONParser p = new JSONParser();
                InputStreamReader inp = new InputStreamReader(input);
                Hashtable h = p.parse(inp);
                allTunes = (Vector<Hashtable>) h.get("root");
                System.out.println(" the search result ............." + allTunes);
            }
        };

        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("Loading...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

        String url = "http://www.mobile-hook.com/api/callertune_search.php?search=" + text;
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);



        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    private void killNetworkAccess() {
        Enumeration e = NetworkManager.getInstance().enumurateQueue();
        if (e.hasMoreElements()) {
            ConnectionRequest r = (ConnectionRequest) e.nextElement();
            r.kill();
        }
    }

    private void fetchTunes(String start) {

        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                JSONParser p = new JSONParser();
                InputStreamReader inp = new InputStreamReader(input);
                Hashtable h = p.parse(inp);
                allTunes = (Vector<Hashtable>) h.get("root");
                //System.out.println(h.toString());

            }
        };

        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("Loading...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

        String url = "http://www.mobile-hook.com/api/callertune_list.php?start=" + start;
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);



        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    private void fetchTopTenTunes() {

        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                JSONParser p = new JSONParser();
                InputStreamReader inp = new InputStreamReader(input);
                Hashtable h = p.parse(inp);
                allTopTunes = (Vector<Hashtable>) h.get("root");
                //System.out.println(h.toString());

            }
        };

        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
//                if (Display.getInstance().getCurrent() instanceof Dialog) {
//                    ((Dialog) Display.getInstance().getCurrent()).dispose();
//                }
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
                //evt.consume();
                //current = ;

            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("fetching...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);


        String url = "http://www.mobile-hook.com/api/topten.php";
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);


        // NetworkManager manager = NetworkManager.getInstance();
        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    private void fetchAllGists(int tuneBegining1) {
        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                try {
                    JSONParser p = new JSONParser();
                    // p = StringUtil.replaceAll(p, "\\n", "\n");
                    InputStreamReader inp = new InputStreamReader(input);
                    Hashtable h = p.parse(inp);//StringUtil.replaceAll(h.get("root"), "\n\n", "\n")
                    allGists = (Vector<Hashtable>) h.get("root");
                    //System.out.println(allGists);
                } catch (Exception e) {
                    Dialog.show("Error Encountered", e.getMessage(), "OK", null);
                }
            }
        };


        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("fetching...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

        String url = "http://www.mobile-hook.com/api/hook_gist.php?start=" + tuneBegining1;
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);


        //NetworkManager manager = NetworkManager.getInstance();
        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    private void subscribeMe(String number, String deService) {

        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                result = Result.fromContent(input, Result.XML);
//   String result = Util.readToString(input);
                //System.out.println(result.getAsString("/response"));

            }
        };


        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("fetching...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

        String url = "http://107.20.238.53/subengine/Default.aspx?sourcemsisdn=" + number + "&servicemsisdn=38051&optional=mobilehookweb&text=" + deService;
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);


        //NetworkManager manager = NetworkManager.getInstance();
        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    private void subscribeVibeChat(String nickname, String msisdn, String age, String sex, String location) {
// http://41.215.140.126/vibechat/index.php?nickname={$nickname}&msisdn={$msisdn}&age={$age}&sex={$sex}&location={$location}&Username=vibechat&Password=v1b3chat       

        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResponseCode());

                //result = Result.fromContent(input, Result.XML);
                vibeResult = Util.readToString(input);
                // System.out.println(vibeResult);

            }
        };


        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //killNetworkAccess();
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                manager.killAndWait(request);
                // killNetworkAccess();
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("fetching...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

// http://41.215.140.126/vibechat/index.php?nickname={$nickname}&msisdn={$msisdn}&age={$age}&sex={$sex}&location={$location}&Username=vibechat&Password=v1b3chat       
        String url = "http://41.215.140.126/vibechat/index.php?nickname=" + nickname + "&msisdn=" + msisdn + "&age=" + age + "&location=" + location + "&sex=" + sex + "&Username=vibechat&Password=v1b3chat";
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        request.setDisposeOnCompletion(d);


        //NetworkManager manager = NetworkManager.getInstance();
        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    @Override
    protected void beforeTopTenTunes(final Form f) {
        // f.setTitle(" ");
        f.setScrollable(false);
        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        //f.setTitle("Top Ten Tunes");

        findHomeButton1(f).setIcon(home_button);
        findHomeButton1(f).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                showForm("Main", null);
            }
        });
        //findMobileHookButton(f).setIcon(mh_button);
        findContainer1(f).getStyle().setBgImage(bgwhite);
        findContainer1(f).getStyle().setBorder(null);
        findContainer1(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);
        //allTopTunes
//        a //findArtistNameAndSong(f).setText("Kukere Kukere");

        ShareButton sb = new ShareButton();
        sb.setTextToShare("MOBILE HOOK: Naija Top ten caller tunes : http://www.mobile-hook.com/mobileweb/toptencallertunes.php");
        findContainer1(f).addComponent(BorderLayout.EAST, sb);

        Container c = findAllTopTenTunes(f);

        c.removeAll();
        for (int i = 0; i < allTopTunes.size(); i++) {
            Hashtable h = allTopTunes.get(i);
            try {
                c.addComponent(addTopTenTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                        h.get("song_rank").toString(), h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                        h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
            } catch (Exception e) {
                TextArea t = new TextArea("An error has been encountered trying to display the Top ten tunes ,please be patient");
                t.setEditable(false);
                t.setUIID("anodaText");
                Dialog dlg = new Dialog("Please be Patient");
                dlg.addComponent(t);
                dlg.setTimeout(1000);
                dlg.show();
            }
        }

        Command about = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);

            }
        };

        f.addCommand(about);

    }

    public Container addTopTenTune(int i, final String artist, final String artistImage, final String tune_name, final String song_rank, final String tune_id, final String mtn_code, final String glo_code,
            final String etisalat_code, final String airtel_code) {

        Resources res = fetchResourceFile();
        Container c = createContainer(res, "TopTenRenderer");
        Label l = findArtistPix(c);

//        Label l1 = findArtisName(c);
//        l1.setText(artist); //+"\n"+h.get("artist").toString()
        ImageDownloadService.createImageToStorage(artistImage, l, artist, new Dimension(48, 48));

        MultiButton b = findEachTopTen(c);
        b.setTextLine1(tune_name);
        b.setTextLine2(artist);
        findSongRank(c).setText(song_rank);
        // b.setTextLine3(song_rank);
        b.setCommand(new Command(tune_name + " " + artist) {
            @Override
            public void actionPerformed(ActionEvent ev) {

                Command[] cmds = new Command[2];
                cmds[0] = new Command("Get") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        song = new Songs(artist, artistImage, tune_name, tune_id, mtn_code, glo_code, etisalat_code, airtel_code);

                        if ((myNumber == null) || ("".equals(myNumber))) {
                            sendingForm = "tune";
                            showForm("AddNumber", null);
                        } else {
                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("Yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                                    if ((myNumber.startsWith("0803")) || (myNumber.startsWith("234803")) || (myNumber.startsWith("0703")) || (myNumber.startsWith("234703")) || (myNumber.startsWith("0806"))
                                            || (myNumber.startsWith("234806")) || (myNumber.startsWith("0706")) || (myNumber.startsWith("234706")) || (myNumber.startsWith("0810")) || (myNumber.startsWith("234810")) || (myNumber.startsWith("0813"))
                                            || (myNumber.startsWith("234813")) || (myNumber.startsWith("0816")) || (myNumber.startsWith("234816")) || (myNumber.startsWith("234814")) || (myNumber.startsWith("0814"))) {
                                        myNetwork = "MTN";

                                        if (song.getMtn_code() == null || ("".equals(song.getMtn_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);

                                        } else {
                                            String res = sendMessageWith("4100", song.getMtn_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0805")) || (myNumber.startsWith("234805")) || (myNumber.startsWith("0705")) || (myNumber.startsWith("234705")) || (myNumber.startsWith("0807"))
                                            || (myNumber.startsWith("234807")) || (myNumber.startsWith("0815")) || (myNumber.startsWith("234815"))) {
                                        myNetwork = "GLO";


                                        if (song.getGlo_code() == null || ("".equals(song.getGlo_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("7728", "tune" + song.getGlo_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0809")) || (myNumber.startsWith("234809")) || (myNumber.startsWith("0817")) || (myNumber.startsWith("234817")) || (myNumber.startsWith("0818")) || (myNumber.startsWith("234818"))) {
                                        myNetwork = "ETISALAT";


                                        if (song.getEtisalat_code() == null || ("".equals(song.getEtisalat_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("251", "download" + song.getEtisalat_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0808")) || (myNumber.startsWith("234808")) || (myNumber.startsWith("0708")) || (myNumber.startsWith("234708")) || (myNumber.startsWith("0802"))
                                            || (myNumber.startsWith("234802")) || (myNumber.startsWith("0812")) || (myNumber.startsWith("234812"))) {
                                        myNetwork = "AIRTEL";

                                        if (song.getAirtel_code() == null || ("".equals(song.getAirtel_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("791", "Buy " + song.getAirtel_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }
                                    } else {
                                        ((Dialog) Display.getInstance().getCurrent()).dispose();
                                        Dialog.show(myNumber, "Could not get the network provider", "OK", null);
                                        //showForm("EnterVendor", null);
                                    }
                                }
                            };

                            cmds[1] = new Command("No") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                                    ((Dialog) Display.getInstance().getCurrent()).dispose();
                                    sendingForm = "tune";
                                    showForm("AddNumber", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText(myNumber);
                            text.setEditable(false);
                            Dialog.show("Your Number?", text, cmds);

                        }

                    }
                };
                cmds[1] = new Command("Cancel") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                    }
                };


                TextArea area = new TextArea();
                area.setUIID("VKBtooltip");
                area.setEditable(false);
                area.setText("you will be charged NGN 50.00 for this operation");
                Dialog.show(tune_name, area, cmds);

                //Dialog.show("", "you will be charged NGN 50.00 for this operation", "OK", null);
            }
        });
        return c;

    }

    @Override
    protected void onMain_ShowTopTenAction(Component c, ActionEvent event) {

        if ((allTopTunes == null) || (allTopTunes.isEmpty())) {
            this.fetchTopTenTunes();
            if ("200".equals(status)) {
                if ((allTopTunes == null) || (allTopTunes.size() <= 0)) {

                    Dialog.show("", "tunes were not fetched", "OK", null);

                    if (Storage.getInstance().exists("allTopTunes")) {
                        allTopTunes = (Vector<Hashtable>) Storage.getInstance().readObject("allTopTunes");
                        showForm("TopTenTunes", null);
                    }

                } else {

                    try {
                        Storage.getInstance().writeObject("allTopTunes", allTopTunes);
                        //showForm("AllTunes", null);
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", e.getMessage(), "OK", null);
                    }

                    showForm("TopTenTunes", null);
                }

            } else {
                Dialog.show("", "you may not be connected to the internet", "OK", null);

                if (Storage.getInstance().exists("allTopTunes")) {
                    allTopTunes = (Vector<Hashtable>) Storage.getInstance().readObject("allTopTunes");
                    showForm("TopTenTunes", null);
                }

            }
        } else {
            showForm("TopTenTunes", null);
        }

    }

    @Override
    protected void beforeRefreshForm(final Form f) {

        f.getContentPane().addPullToRefresh(new Runnable() {
            public void run() {
                f.addComponent(0, new Button("Added " + f.getContentPane().getComponentCount()));
                f.revalidate();
            }
        });
    }

    @Override
    protected void onMain_AllOtherTunesAction(Component c, ActionEvent event) {

        if ((allTunes == null) || (allTunes.isEmpty())) {

            tuneBegining = 0;
            pageNo = 1;

            this.fetchTunes(String.valueOf(tuneBegining));
            if ("200".equals(status)) {
                //try {
                //tuneSize = allTopTunes.size();
                if ((allTunes == null) || (allTunes.size() <= 0)) {
                    Dialog.show("", "tunes were not fetched", "OK", null);
                    if (Storage.getInstance().exists("allTunes")) {
                        try {
                            allTunes = (Vector<Hashtable>) Storage.getInstance().readObject("allTunes");
                            showForm("AllTunes", null);
                        } catch (Exception e) {
                            Dialog.show("oh dear!!!", e.getMessage(), "OK", null);
                        }

                    }

                } else {

                    try {
                        Storage.getInstance().writeObject("allTunes", allTunes);
                        //showForm("AllTunes", null);
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", e.getMessage(), "OK", null);
                    }

                    showForm("AllTunes", null);
                }
//                } catch (Exception e) {
//                    Dialog.show("", "connection may have been canelled", "OK", null);
//                }
            } else {
                Dialog.show("Oh dear", "you may not be connected to the internet", "OK", null);

                if (Storage.getInstance().exists("allTunes")) {
                    try {
                        allTunes = (Vector<Hashtable>) Storage.getInstance().readObject("allTunes");
                        showForm("AllTunes", null);
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", e.getMessage(), "OK", null);
                    }
                }
            }
        } else {
            showForm("AllTunes", null);
        }
    }

    private String sendMessageWith(String networkCode, String songCode) {

        try {
            Display.getInstance().sendSMS(networkCode, songCode);
            response = "success";
        } catch (Exception e) {
            response = e.getMessage();
        }

        return response;
    }

    /**
     *
     * @param h
     * @return
     */
    public Container addTune(int i, final String artist, final String artistImage, final String tune_name, final String tune_id, final String mtn_code, final String glo_code,
            final String etisalat_code, final String airtel_code) {

        Resources res = fetchResourceFile();
        Container c = createContainer(res, "EachTune");
        Label l = findArtistImage(c);

//        Label l1 = findArtisName(c);
//        l1.setText(artist); //+"\n"+h.get("artist").toString()
        ImageDownloadService.createImageToStorage(artistImage, l, artist, new Dimension(48, 48));

        MultiButton b = findTuneAndArtistName(c);
        b.setTextLine1(tune_name);
        b.setTextLine2(artist);
        b.setCommand(new Command(tune_name + "\n" + artist) {
            @Override
            public void actionPerformed(ActionEvent ev) {

                Command[] cmds = new Command[2];
                cmds[0] = new Command("Get") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        song = new Songs(artist, artistImage, tune_name, tune_id, mtn_code, glo_code, etisalat_code, airtel_code);

                        if ((myNumber == null) || ("".equals(myNumber))) {
                            sendingForm = "tune";
                            showForm("AddNumber", null);
                        } else {

                            //Dialog.show("Your Number", myNumber, "OK", null);
                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("Yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                                    if ((myNumber.startsWith("0803")) || (myNumber.startsWith("234803")) || (myNumber.startsWith("0703")) || (myNumber.startsWith("234703")) || (myNumber.startsWith("0806"))
                                            || (myNumber.startsWith("234806")) || (myNumber.startsWith("0706")) || (myNumber.startsWith("234706")) || (myNumber.startsWith("0810")) || (myNumber.startsWith("234810")) || (myNumber.startsWith("0813"))
                                            || (myNumber.startsWith("234813")) || (myNumber.startsWith("0816")) || (myNumber.startsWith("234816")) || (myNumber.startsWith("234814")) || (myNumber.startsWith("0814"))) {
                                        myNetwork = "MTN";

                                        if (song.getMtn_code() == null || ("".equals(song.getMtn_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);

                                        } else {
                                            String res = sendMessageWith("4100", song.getMtn_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0805")) || (myNumber.startsWith("234805")) || (myNumber.startsWith("0705")) || (myNumber.startsWith("234705")) || (myNumber.startsWith("0807"))
                                            || (myNumber.startsWith("234807")) || (myNumber.startsWith("0815")) || (myNumber.startsWith("234815"))) {
                                        myNetwork = "GLO";


                                        if (song.getGlo_code() == null || ("".equals(song.getGlo_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("7728", "tune" + song.getGlo_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0809")) || (myNumber.startsWith("234809")) || (myNumber.startsWith("0817")) || (myNumber.startsWith("234817")) || (myNumber.startsWith("0818")) || (myNumber.startsWith("234818"))) {
                                        myNetwork = "ETISALAT";


                                        if (song.getEtisalat_code() == null || ("".equals(song.getEtisalat_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("251", "download" + song.getEtisalat_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }

                                    } else if ((myNumber.startsWith("0808")) || (myNumber.startsWith("234808")) || (myNumber.startsWith("0708")) || (myNumber.startsWith("234708")) || (myNumber.startsWith("0802"))
                                            || (myNumber.startsWith("234802")) || (myNumber.startsWith("0812")) || (myNumber.startsWith("234812"))) {
                                        myNetwork = "AIRTEL";

                                        if (song.getAirtel_code() == null || ("".equals(song.getAirtel_code()))) {
                                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                                            Dialog.show("Oh dear", "Does not apply to your network", "OK", null);
                                        } else {
                                            String res = sendMessageWith("791", "Buy " + song.getAirtel_code());
                                            if ("success".equals(res)) {
                                                ((Dialog) Display.getInstance().getCurrent()).dispose();
                                                Dialog.show("", "Successfully subscribed", "OK", null);

                                            } else {
                                                Dialog.show("", res, "OK", null);
                                            }
                                        }
                                    } else {
                                        ((Dialog) Display.getInstance().getCurrent()).dispose();
                                        Dialog.show(myNumber, "Could not get the network provider", "OK", null);
                                        //showForm("EnterVendor", null);
                                    }
                                }
                            };

                            cmds[1] = new Command("No") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                                    ((Dialog) Display.getInstance().getCurrent()).dispose();
                                    sendingForm = "tune";
                                    showForm("AddNumber", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText(myNumber);
                            text.setEditable(false);
                            Dialog.show("Your Number?", text, cmds);
                        }

                    }
                };
                cmds[1] = new Command("Cancel") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                    }
                };


                TextArea area = new TextArea();
                //area.setUIID("VKBtooltip");
                area.setEditable(false);
                area.setText("you will be charged NGN 50.00 for this operation");
                Dialog.show(tune_name, area, cmds);

                //Dialog.show("", "you will be charged NGN 50.00 for this operation", "OK", null);
            }
        });
        return c;

    }

    @Override
    protected void beforeAllTunes(final Form f) {
        f.setScrollable(false);
        f.setTitle("All Tunes");
        Container c = findAllTunesContainer(f);
        //tuneSize = allTunes.size();
        for (int i = 0; i < allTunes.size(); i++) {
            Hashtable h = allTunes.elementAt(i);
            //System.out.println(i +" "+h);
            try {
                c.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                        h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                        h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
            } catch (Exception e) {
                //System.out.println(i +" "+h);
                TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                t.setEditable(false);
                t.setUIID("anodaText");
                Dialog dlg = new Dialog("Please be Patient");
                dlg.addComponent(t);
                dlg.setTimeout(1000);
                dlg.show();
            }
        }

        if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
            //Dialog.show("Wait", "You are at the first page", "OK", null);
            findPreviousTunes(f).setEnabled(false);
        } else {
            findPreviousTunes(f).setEnabled(true);
        }

        findTunePageNumber(f).setText("Page " + String.valueOf(pageNo));

        Command home = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);

            }
        };

        f.addCommand(home);

        Command ref = new Command("Refresh") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                tuneBegining = 0;

                fetchTunes(String.valueOf(tuneBegining));
                if ("200".equals(status)) {
                    if ((allTunes == null) || (allTunes.isEmpty())) {
                        Dialog.show("oh!! dear", "Tunes were not fetched", "OK", null);
                    } else {

                        Container c2 = findAllTunesContainer(f);
                        c2.removeAll();

                        for (int i = 0; i < allTunes.size(); i++) {
                            Hashtable h = allTunes.elementAt(i);
                            //System.out.println(h);
                            try {
                                c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                                        h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                                        h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
                            } catch (Exception e) {
                                TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                                t.setEditable(false);
                                t.setUIID("anodaText");
                                Dialog dlg = new Dialog("Please be Patient");
                                dlg.addComponent(t);
                                dlg.setTimeout(1000);
                                dlg.show();
                            }
                        }

                        pageNo = 1;
                        findTunePageNumber(f).setText("Page " + String.valueOf(pageNo));
                        if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
                            //Dialog.show("Wait", "You are at the first page", "OK", null);
                            findPreviousTunes(f).setEnabled(false);
                        } else {
                            findPreviousTunes(f).setEnabled(true);
                        }

                        f.revalidate();//animateLayoutAndWait(5000);
                    }

                } else {
                    Dialog.show("oh Dear", "Could not fetch tunes", "OK", null);
                }

            }
        };

        f.addCommand(ref);
    }

    @Override
    protected void beforeMain(Form f) {
        f.setTitle("Mobile Hook");
        Display.getInstance().unlockOrientation();

//        f.revalidate();
//        Container c4 = findContainer4(f);
//
//        for (int iter = 0; iter < c4.getComponentCount(); iter++) {
//            Component current = c4.getComponentAt(iter);
//            if (iter % 2 == 0) {
//                current.setX(-current.getWidth());
//            } else {
//                current.setX(current.getWidth());
//            }
//        }
//        c4.setShouldCalcPreferredSize(true);
//        c4.animateLayout(2000);

        //MediaManager m = 
        f.setScrollable(false);
        //System.out.println(Display.getInstance().getPlatformName());

        // Storage.getInstance().clearStorage();
        //  Storage.getInstance().deleteStorageFile("08034453370");
        // Storage.getInstance().deleteStorageFile("MyNumber");
        if (Storage.getInstance().exists("MyNumber")) {
            myNumber = (String) Storage.getInstance().readObject("MyNumber");
        } else {
            if (Display.getInstance().getMsisdn() != null) {
                myNumber = Display.getInstance().getMsisdn();
            }

        }
        Command close = new Command("Exit") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                Display.getInstance().exitApplication();

            }
        };


        f.addCommand(close);

        Command about = new Command("About") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("About", null);

            }
        };

        f.addCommand(about);
//         
//        ShareButton sb = new ShareButton();
//        if(Display.getInstance().getPlatformName().equals("ios")){
//            sb.setTextToShare("");
//        }else if(Display.getInstance().getPlatformName().equals("and")){
//            sb.setTextToShare("");
//        }else if(Display.getInstance().getPlatformName().equals("me")){
//            sb.setTextToShare("");
//        }else if(Display.getInstance().getPlatformName().equals("rim")){
//            sb.setTextToShare("");
//        }else{
//            sb.setTextToShare("http://www.mobile-hook.com");
//        }
//        
//        findContainer7(f).addComponent(BorderLayout.EAST, sb);

//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

//        findLabel2(f).setText(" ");
//        findHomeButton(f).setIcon(home_button);
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer7(f).getStyle().setBgImage(bgwhite);
//        findContainer7(f).getStyle().setBorder(null);
//        findContainer7(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Image holla = fetchResourceFile().getImage("holla_back.jpg");
        Image hook = fetchResourceFile().getImage("hook_gist.jpg");
        Image more_tunes = fetchResourceFile().getImage("more_caller_tunes.jpg");
        Image news = fetchResourceFile().getImage("news_and_life_style.jpg");
        Image top_ten = fetchResourceFile().getImage("top_ten.jpg");
        Image videos = fetchResourceFile().getImage("videos.jpg");
        Image faq = fetchResourceFile().getImage("faq.jpg");

        //findTopTenLbl(f).setIcon(top_ten.scaled((Display.getInstance().getDisplayHeight() / 6), (Display.getInstance().getDisplayWidth() / 6)));
        findTopTenLbl(f).setIcon(top_ten.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findMoreTunesLbl(f).setIcon(more_tunes.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findVideosLbl(f).setIcon(videos.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findGistLbl(f).setIcon(hook.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findNewsLifeStyleLbl(f).setIcon(news.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findFaqLbl(f).setIcon(faq.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        findHollaLbl(f).setIcon(holla.scaledWidth(Display.getInstance().getDisplayWidth() / 7));
        //findHollaLbl(f).setIcon(Graphics g = new java.awt.Graphics);




    }

    @Override
    protected void onMain_GistsAction(Component c, ActionEvent event) {

        //showForm("AllGists", null);
        if ((allGists == null) || (allGists.isEmpty())) {
            gistBegining = 0;
            pageNo = 1;
            this.fetchAllGists(gistBegining);
            if ("200".equals(status)) {
                if ((allGists == null) || (allGists.isEmpty())) {

                    Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);

                    if (Storage.getInstance().exists("allGists")) {
                        allGists = (Vector<Hashtable>) Storage.getInstance().readObject("allGists");
                        showForm("AllGists", null);
                        //showForm("Gists2", null);
                    }

                } else {

                    try {
                        Storage.getInstance().writeObject("allGists", allGists);
                        //showForm("AllTunes", null);
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", e.getMessage(), "OK", null);
                    }
//                    final Image placeholder = fetchResourceFile().getImage("user.png");
//                    for (int i = 0; i < allGists.size(); i++) {
//                        Hashtable hashtable = allGists.elementAt(i);
//                        hashtable.put("placeholder", placeholder);
//                    }
                    showForm("AllGists", null);
                    //showForm("Gists2", null);
                }

            } else {
                Dialog.show("oh!! dear", "you may not be connected to the internet", "OK", null);

                if (Storage.getInstance().exists("allGists")) {
                    allGists = (Vector<Hashtable>) Storage.getInstance().readObject("allGists");
                    showForm("AllGists", null);
                }

            }
        } else {
            showForm("AllGists", null);
        }
    }

    public Container addGist(final String gist_title, final String gist_content, final String gist_time, final String gist_image, final String gist_id, final String shorturl) {
        Resources res = fetchResourceFile();
        Container c = createContainer(res, "GistRenderer");

        MultiButton b = (MultiButton) findGistRendererTitle(c);
        // b.set
        b.setTextLine1(gist_title);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                gist = new Gists(gist_image, gist_title, gist_id, gist_time, StringUtil.replaceAll(gist_content, "'n", "\n"), shorturl);
                showForm("SelectedGist", null);
//                Dialog.show("", gist_title, "OK", null);
//                evt.consume();
            }
        });
        //findGistTime(c).setText(gist_time);
        //findGistContent(c).setText(gist_content);
        final Label l = findGistImage(c);

        ImageDownloadService.createImageToStorage(gist_image, l, gist_id, new Dimension((Display.getInstance().getDisplayHeight() / 4), (Display.getInstance().getDisplayWidth() / 5)));

//        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readResponse(InputStream input) throws IOException {
//
//                EncodedImage image = EncodedImage.create(input);
//                l.setIcon(image.scaled(Display.getInstance().getDisplayHeight() / 4, Display.getInstance().getDisplayWidth() / 5));
//
//            }
//        };
//
//
//        final NetworkManager manager = NetworkManager.getInstance();
//
//
//        //String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=false";
//        request.setUrl(gist_image);
//
//        request.setFailSilently(true);//stops user from seeing error message on failure
//        request.setPost(false);
//        request.setDuplicateSupported(true);
//
//        manager.start();
//        //manager.setTimeout(5000);
//        manager.addToQueue(request);
        return c;
    }

    @Override
    protected void beforeAllGists(final Form f) {
        f.setTitle("Gists");
       
//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
//        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");
//
//        findLabel(f).setText(" ");
//        findHomeButton(f).setIcon(home_button);
//        findHomeButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer(f).getStyle().setBgImage(bgwhite);
//        findContainer(f).getStyle().setBorder(null);
//        findContainer(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);


        f.setScrollable(false);
        Command home = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);
                //Display.getInstance().exitApplication();
            }
        };


        f.addCommand(home);

        Command next = new Command("Next Page") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.

                gistBegining += 20;

                fetchAllGists(gistBegining);
                if ("200".equals(status)) {
                    if ((allGists == null) || (allGists.isEmpty())) {
                        Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);
                    } else {
                        pageNo += 1;
                        Container c2 = findAllGists(f);
                        //tuneSize = allTunes.size();
                        c2.removeAll();
                        for (int i = 0; i < allGists.size(); i++) {
                            Hashtable h = allGists.get(i);
                            try {
                                c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                                        h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
                            } catch (Exception e) {
                                TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                                t.setEditable(false);
                                t.setUIID("anodaText");
                                Dialog dlg = new Dialog("Please be Patient");
                                dlg.addComponent(t);
                                dlg.setTimeout(1000);
                                dlg.show();
                            }
                        }
//                        final Image placeholder = fetchResourceFile().getImage("user.png");
//                        final List list = findGistList(f);
//                        //initListModelGistList(list);
//                        //list.getModel();
//
//                        list.setRenderer(new GistRenderer());
//                        for (int i = 0; i < allGists.size(); i++) {
//                            Hashtable h = allGists.get(i);
//                            h.put("placeholder", placeholder);
//                            //list.addItem(h);
//                            //addAvatar(list, h, i);
//                        }
//                        list.setModel(new DefaultListModel(allGists));
//                        Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                        Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                        list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//                            @Override
//                            public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                                Dimension postIconSize = new Dimension(Display.getInstance().getDisplayHeight()/5, Display.getInstance().getDisplayHeight()/4);
//                                for (int i = 0; i < allGists.size(); i++) {
//                                    Hashtable h = allGists.get(i);
//                                    h.put("placeholder", placeholder);
//                                    //list.addItem(h);
//                                    addAvatar(list, h, i);
//                                }
//                                return super.getListCellRendererComponent(list, value, index, isSelected);
//                            }
//                        });

                        findPageNumber(f).setText("Page " + String.valueOf(pageNo));
                        if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                            //Dialog.show("Wait", "You are at the first page", "OK", null);
                            findPrevGist(f).setEnabled(false);
                        } else {
                            findPrevGist(f).setEnabled(true);
                        }

                        f.revalidate();//animateLayoutAndWait(5000);
                    }

                }
            }
        };

        f.addCommand(next);

        if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
            //Dialog.show("Wait", "You are at the first page", "OK", null);
            findPrevGist(f).setEnabled(false);
        } else {
            findPrevGist(f).setEnabled(true);

            Command prev = new Command("Prev Page") {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.

                    if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                        //Dialog.show("Wait", "You are at the first page", "OK", null);
                        findPrevGist(f).setEnabled(false);

                    } else {

                        gistBegining -= 20;

                        fetchAllGists(gistBegining);

                        if ("200".equals(status)) {
                            if ((allGists == null) || (allGists.isEmpty())) {
                                Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);
                            } else {
                                pageNo -= 1;
                                Container c2 = findAllGists(f);
                                //tuneSize = allTunes.size();
                                c2.removeAll();
                                for (int i = 0; i < allGists.size(); i++) {
                                    Hashtable h = allGists.get(i);
                                    try {
                                        c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                                                h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
                                    } catch (Exception e) {
                                        TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                                        t.setEditable(false);
                                        t.setUIID("anodaText");
                                        Dialog dlg = new Dialog("Please be Patient");
                                        dlg.addComponent(t);
                                        dlg.setTimeout(1000);
                                        dlg.show();
                                    }
                                }
//                                final List list = findGistList(f);
//                                list.setModel(new DefaultListModel(allGists));
//                                Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                                Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                                list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//                                    @Override
//                                    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                                        for (int i = 0; i < allGists.size(); i++) {
//                                            Hashtable h = allGists.get(i);
//                                            //list.addItem(h);
//                                            addAvatar(list, h, i);
//                                        }
//                                        return super.getListCellRendererComponent(list, value, index, isSelected);
//                                    }
//                                });

                                findPageNumber(f).setText("Page " + String.valueOf(pageNo));
                                if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                                    //Dialog.show("Wait", "You are at the first page", "OK", null);
                                    findPrevGist(f).setEnabled(false);
                                }
                                f.revalidate();//animateLayoutAndWait(5000);
                            }

                        }

                    }
                }
            };

            f.addCommand(prev);
            //f.revalidate();
        }

        Container c = findAllGists(f);
        //tuneSize = allTunes.size();
        c.removeAll();
        for (int i = 0; i < allGists.size(); i++) {
            Hashtable h = allGists.get(i);
            try {
                c.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                        h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
            } catch (Exception e) {
                TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                t.setEditable(false);
                t.setUIID("anodaText");
                Dialog dlg = new Dialog("Please be Patient");
                dlg.addComponent(t);
                dlg.setTimeout(1000);
                dlg.show();
            }
        }

//        super.beforeAllGists(f);//Gists2(f);//MainForm(f);
//        final List list = findGistList(f);
//        //list.setModel(new DefaultListModel(allGists));
//        Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//        Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//        list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//            @Override
//            public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                for (int i = 0; i < allGists.size(); i++) {
//                    Hashtable h = allGists.get(i);
//                    //list.addItem(h);
//                    addAvatar(list, h, i);
//                }
//                return super.getListCellRendererComponent(list, value, index, false);
//            }
//        });

        //final Image placeholder = fetchResourceFile().getImage("user.png");
        //  final List list = findGistList(f);
        //initListModelGistList(list);
        //list.getModel();

//        for (int i = 0; i < allGists.size(); i++) {
//            Hashtable h = allGists.get(i);
//            h.put("placeholder", placeholder);
//            //list.addItem(h);
//            //addAvatar(list, h, i);
//        }

//        list.setRenderer(new GistRenderer());
//        list.setModel(new DefaultListModel(allGists));
        //list.getModel();
        findPageNumber(f).setText("Page " + String.valueOf(pageNo));
//        super.beforeAllGists(f);//MainForm(f);
//        final List list = findGistList(f);
//        Component selected = createContainer(fetchResourceFile(), "AGist");
//        Component unselected = createContainer(fetchResourceFile(), "AGist");
//
//        list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//            @Override
//            public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                if (((index + 1) >= list.size()) && (!isFinished == true)) {
//                    fetchMore(list, gistBegining += 10);
//                }
//                return super.getListCellRendererComponent(list, value, index, isSelected);
//            }
//        });

        Command ref = new Command("Refresh") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                gistBegining = 0;

                fetchAllGists(gistBegining);
                if ("200".equals(status)) {
                    if ((allGists == null) || (allGists.isEmpty())) {
                        Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);
                    } else {
                        try {
                            Container c2 = findAllGists(f);
                            //tuneSize = allTunes.size();
                            c2.removeAll();
                            for (int i = 0; i < allGists.size(); i++) {
                                Hashtable h = allGists.get(i);
                                try {
                                    c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                                            h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
                                } catch (Exception e) {
                                    TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                                    t.setEditable(false);
                                    t.setUIID("anodaText");
                                    Dialog dlg = new Dialog("Please be Patient");
                                    dlg.addComponent(t);
                                    dlg.setTimeout(1000);
                                    dlg.show();
                                }
                            }

//                            final List list = findGistList(f);
//                            list.setModel(new DefaultListModel(allGists));
//                            Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                            Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                            list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//                                @Override
//                                public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                                    for (int i = 0; i < allGists.size(); i++) {
//                                        Hashtable h = allGists.get(i);
//                                        //list.addItem(h);
//                                        addAvatar(list, h, i);
//                                    }
//                                    return super.getListCellRendererComponent(list, value, index, isSelected);
//                                }
//                            });

                            pageNo = 1;
                            findPageNumber(f).setText("Page " + String.valueOf(pageNo));
                            if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                                //Dialog.show("Wait", "You are at the first page", "OK", null);
                                findPrevGist(f).setEnabled(false);
                            } else {
                                findPrevGist(f).setEnabled(true);
                            }

                            f.revalidate();//animateLayoutAndWait(5000);
                        } catch (Exception e) {
                        }

                        //catch(
                    }

                }

            }
        };

        f.addCommand(ref);
        
        
        
    }

    private void fetchMore(final List cmp, int tuneBegining1) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        final ConnectionRequest request = new ConnectionRequest() {
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//
//                status = getHeader(connection, null);
//                // System.out.println("The status of the connection: " + status);
//            }
//            //*****************
            @Override
            protected void readResponse(InputStream input) throws IOException {

                status = String.valueOf(getResposeCode());

                JSONParser p = new JSONParser();
                //InputStreamReader inp = new InputStreamReader(input);
                try {
                    allGists = (Vector) p.parse(new InputStreamReader(input)).get("root");
                } catch (Exception e) {
                    Dialog.show("", "'" + e.getMessage() + "'" + " no more data to fetch", "OK", null);
                }
//                Hashtable h = p.parse(inp);
                if (allGists.isEmpty()) {
                    isFinished = true;
                } else {
//                    System.out.println(allGists.toString());
//                    System.out.println("******************************************************************************");
//                    // allGists = (Vector<Hashtable>) h.get("root");
                    //System.out.println(h.toString());

                    int count = cmp.size();
                    if (allGists.size() > 0) {
                        for (int i = 0; i < allGists.size(); i++) {
                            Hashtable gist = (Hashtable) allGists.elementAt(i);
                            cmp.addItem(gist);
                            addAvatar(cmp, gist, count + i);
                        }
                    }
                }
            }

            private void addAvatar(List list, Hashtable gist, int i) {
                String url = (String) gist.get("gist_image");
                String id = (String) gist.get("gist_title");
                if (url == null || url.startsWith("http:") == false) {
                    // ImageDownloadService doesn't support HTTPS at moment
                    return;
                }
                //ImageDownloadService.createImageToStorage(artistImage, l, artist, new Dimension(48, 48));
                ImageDownloadService.createImageToStorage(url, list, i, "gist_image_real", id + "_avater", new Dimension(48, 48));
            }
        };



        final NetworkManager manager = NetworkManager.getInstance();
        Command c = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                manager.killAndWait(request);
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        //Dialog dlg = ip.showInifiniteBlocking();
        Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("Loading...");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(c);

        String url = "http://www.mobile-hook.com/api/hook_gist.php?start=" + tuneBegining1;
        request.setUrl(url);

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);
        //request.setTimeout(2000);
        //request.setDisposeOnCompletion(d);


        // NetworkManager manager = NetworkManager.getInstance();
        manager.start();
        manager.setTimeout(2000);
        manager.addToQueueAndWait(request);
    }

    @Override
    protected void beforeSelectedGist(Form f) {
        //f.setTitle(" ");
        f.setScrollable(false);
        //;
        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        ShareButton sb = new ShareButton();
        //System.out.println("HOOK GIST: "+gist.getTitle()+" "+gist.getShorturl());
        sb.setTextToShare("HOOK GIST: " + gist.getTitle() + " " + gist.getShorturl());
        findContainer3(f).addComponent(BorderLayout.EAST, sb);

        findLabel(f).setText(" ");
        findHomeButton3(f).setIcon(home_button);
        findHomeButton3(f).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);
            }
        });
        //findMobileHookButton(f).setIcon(mh_button);
        findContainer3(f).getStyle().setBgImage(bgwhite);
        findContainer3(f).getStyle().setBorder(null);
        findContainer3(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        //f.addComponent(addGist(gist.getTitle(), gist.getContent(), gist.getTime_posted(),gist.getImage_url()));
        Command goHome = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);
                //Display.getInstance().exitApplication();
            }
        };


        f.getMenuBar().addCommand(goHome);
        final Label l = findGistImage(f);

        f.addOrientationListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                final ConnectionRequest request = new ConnectionRequest() {
                    @Override
                    protected void readResponse(InputStream input) throws IOException {

                        EncodedImage image = EncodedImage.create(input);
                        l.setIcon(image.scaledWidth(Display.getInstance().getDisplayWidth()));

                    }
                };


                final NetworkManager manager = NetworkManager.getInstance();


                //String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=false";
                request.setUrl(gist.getImage_url());

                request.setFailSilently(true);//stops user from seeing error message on failure
                request.setPost(false);
                request.setDuplicateSupported(true);

                manager.start();
                manager.setTimeout(5000);
                manager.addToQueue(request);
            }
        });
        findGistTitleArea(f).setText(gist.getTitle());
        //;json = StringUtil.replace(json, "\\n", "\n");
        findTimePosted(f).setText(gist.getTime_posted());
        // System.out.println(gist.getContent());
        findGistContent(f).setText(gist.getContent());



        //ImageDownloadService.createImageToStorage(gist.getImage_url(), l, gist.getTitle(), new Dimension((Display.getInstance().getDisplayHeight() / 2), (Display.getInstance().getDisplayWidth() / 2)));

        final ConnectionRequest request = new ConnectionRequest() {
            @Override
            protected void readResponse(InputStream input) throws IOException {

                EncodedImage image = EncodedImage.create(input);
                l.setIcon(image.scaledWidth(Display.getInstance().getDisplayWidth()));

            }
        };


        final NetworkManager manager = NetworkManager.getInstance();


        //String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=false";
        request.setUrl(gist.getImage_url());

        request.setFailSilently(true);//stops user from seeing error message on failure
        request.setPost(false);
        request.setDuplicateSupported(true);

        manager.start();
        manager.setTimeout(5000);
        manager.addToQueue(request);
    }

    @Override
    protected void beforeNewsLifeStyle(final Form f) {
        // f.setTitle(" ");
        f.setScrollable(false);
//
//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        f.setTitle("News and Life Style");
//        findHomeButton(f).setIcon(home_button);
//        findHomeButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer1(f).getStyle().setBgImage(bgwhite);
//        findContainer1(f).getStyle().setBorder(null);
//        findContainer1(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Command goHome = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);
                //Display.getInstance().exitApplication();
            }
        };


        f.addCommand(goHome);

//        f.addOrientationListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                f.animateLayoutAndWait(10000);//repaint();//evalidate();
//                //Dialog.show("Orientation Changed", "Orientation has been changed", "OK", null);
//            }
//        });


        Image newsImage = fetchResourceFile().getImage("breaking_news.jpg");
        Image fbImage = fetchResourceFile().getImage("football_news.jpg");
        Image loveImage = fetchResourceFile().getImage("love_and_sex_tips.jpg");
        Image healthImage = fetchResourceFile().getImage("health_tips.jpg");
        Image jobConn = fetchResourceFile().getImage("job_connect.jpg");
        Image accWeather = fetchResourceFile().getImage("weather.jpg");
        Image entNews = fetchResourceFile().getImage("entertainment_gist.jpg");
        Image dailyScr = fetchResourceFile().getImage("holy_scripture.jpg");
        //Image mbNews = fetchResourceFile().getImage("Feed_Newspaper_256.jpg");
        Image vbChat = fetchResourceFile().getImage("vibe_chat.jpg");
        Image bbHabit = fetchResourceFile().getImage("breaking_bad_habits.jpg");

        findBreakingNews(f).setIcon(newsImage.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findFootballTips(f).setIcon(fbImage.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findLoveANDsex(f).setIcon(loveImage.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findHeathANDfitness(f).setIcon(healthImage.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findJobsConnect(f).setIcon(jobConn.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findAccuWeather(f).setIcon(accWeather.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findEntertainmentGist(f).setIcon(entNews.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findDailyScriptures(f).setIcon(dailyScr.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findVibeChat(f).setIcon(vbChat.scaledWidth(Display.getInstance().getDisplayWidth() / 4));
        findBreakingBadHabit(f).setIcon(bbHabit.scaledWidth(Display.getInstance().getDisplayWidth() / 4));


    }

    @Override
    protected void onMain_NewsAndLifestyleAction(Component c, ActionEvent event) {

        showForm("News_LifeStyle", null).animate();
    }

    @Override
    protected void onAllGists_NextGistAction(Component c, ActionEvent event) {

        gistBegining += 20;

        this.fetchAllGists(gistBegining);
        if ("200".equals(status)) {
            if ((allGists == null) || (allGists.isEmpty())) {
                Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);
            } else {
                try {
                    Container c2 = findAllGists(c.getComponentForm());
                    //tuneSize = allTunes.size();
                    c2.removeAll();
                    for (int i = 0; i < allGists.size(); i++) {
                        Hashtable h = allGists.get(i);
                        try {
                            c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                                    h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
                        } catch (Exception e) {
                            TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                            t.setEditable(false);
                            t.setUIID("anodaText");
                            Dialog dlg = new Dialog("Please be Patient");
                            dlg.addComponent(t);
                            dlg.setTimeout(1000);
                            dlg.show();
                        }
                    }
//                    final List list = findGistList(c.getComponentForm());
//
//                    list.setModel(new DefaultListModel(allGists));
//                    Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                    Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                    //list.setRenderer(new );
//                    list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//                        @Override
//                        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                            for (int i = 0; i < allGists.size(); i++) {
//                                Hashtable h = allGists.get(i);
//                                //list.addItem(h);
//                                addAvatar(list, h, i);
//                            }
//                            return super.getListCellRendererComponent(list, value, index, false);
//                        }
//                    });

                    pageNo += 1;
                    findPageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));
                    if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                        //Dialog.show("Wait", "You are at the first page", "OK", null);
                        findPrevGist(c.getComponentForm()).setEnabled(false);
                    } else {
                        findPrevGist(c.getComponentForm()).setEnabled(true);
                    }

                    c.getComponentForm().revalidate();//animateLayoutAndWait(5000);
                } catch (Exception e) {
                }

                //catch(
            }

        }

    }

    @Override
    protected void onAllGists_PrevGistAction(Component c, ActionEvent event) {

        pageNo -= 1;
        gistBegining -= 20;

        this.fetchAllGists(gistBegining);

        if ("200".equals(status)) {
            if ((allGists == null) || (allGists.isEmpty())) {
                Dialog.show("oh!! dear", "Gists were not fetched", "OK", null);
            } else {
                Container c2 = findAllGists(c.getComponentForm());
                //tuneSize = allTunes.size();
                c2.removeAll();
                for (int i = 0; i < allGists.size(); i++) {
                    Hashtable h = allGists.get(i);
                    try {
                        c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
                                h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
                    } catch (Exception e) {
                        TextArea t = new TextArea("An error has been encountered trying to display the Gists, please be patient");
                        t.setEditable(false);
                        t.setUIID("anodaText");
                        Dialog dlg = new Dialog("Please be Patient");
                        dlg.addComponent(t);
                        dlg.setTimeout(1000);
                        dlg.show();
                    }
                }

//                final List list = findGistList(c.getComponentForm());
//                list.setModel(new DefaultListModel(allGists));
//                Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//                list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//                    @Override
//                    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                        for (int i = 0; i < allGists.size(); i++) {
//                            Hashtable h = allGists.get(i);
//                            //list.addItem(h);
//                            addAvatar(list, h, i);
//                        }
//                        return super.getListCellRendererComponent(list, value, index, isSelected);
//                    }
//                });

                findPageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));
                if (((gistBegining - 20) < 0) || ((pageNo - 1) < 1)) {
                    //Dialog.show("Wait", "You are at the first page", "OK", null);
                    findPrevGist(c.getComponentForm()).setEnabled(false);
                }
                c.getComponentForm().revalidate();//animateLayoutAndWait(5000);
            }

        }

    }

    protected void onNewsLifeStyle3_BackToStyle2Action(Component c, ActionEvent event) {

        back();
    }

    @Override
    protected void onNewsLifeStyle_BreakingNewsAction(Component c, ActionEvent event) {
        service = "news";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Staying updated with happenings around us daily is very important. "
                + "With this service, Breaking News is sent out immediately as it breaks from any part of "
                + "the country before it spreads. This service provides local breaking news alerts "
                + "round the clock to keep subscribers up to date on happenings within their locality and nationwide. ");
        //Dialog.show("Breaking News", area, cmds);

        aLife = new NewsAndLifeStyles("Breaking News", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_LoveANDsexAction(Component c, ActionEvent event) {
        service = "intimate";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Do you want to improve your sex live and make sure your partner keeps coming for more "
                + "and sustain your Love life and relationship? Then this service is designed for you to provide "
                + "you love and sex tips, it is designed to provide you with tips on how to improve your love and "
                + "romance life. ");
        //Dialog.show("Love and Sex Tips", area, cmds);

        aLife = new NewsAndLifeStyles("Love and Sex Tips", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_FootballTipsAction(Component c, ActionEvent event) {
        service = "yes";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        // area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("This is a football news alert service providing subscribers with round the clock "
                + "updates on all football leagues such as EPL, LA LIGA, SERIA A, BUNDESLIGA, UEFA and even "
                + "our Local Nigerian league. The service provides news on match fixtures, scores, transfers "
                + "and breaking news. ");
        //Dialog.show("Football Alert", area, cmds);

        aLife = new NewsAndLifeStyles("Football Alert", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_HeathANDfitnessAction(Component c, ActionEvent event) {
        service = "wlt";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Like the saying goes Health is Wealth, living in good health and staying fit is "
                + "serious business. This service provides you with good tips and ideas on how to stay "
                + "fit and maintain a healthy weight. This services has been tested and is recomemded for you. ");
        //Dialog.show("Health and Fitness Tips", area, cmds);

        aLife = new NewsAndLifeStyles("Health and Fitness Tips", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onTopTenTunes_ShowMoreTunesAction(Component c, ActionEvent event) {

        if ((allTunes == null) || (allTunes.isEmpty())) {
            tuneBegining = 0;
            pageNo = 1;
            this.fetchTunes(String.valueOf(tuneBegining));
            //tuneSize = allTopTunes.size();
            if ((allTunes == null) || (allTunes.size() <= 0)) {
                Dialog.show("", "tunes were not fetched", "OK", null);
            } else {
                // System.out.println(allTunes);
                showForm("AllTunes", null);
            }

        } else {
            showForm("AllTunes", null);
        }
    }

    @Override
    protected void beforeEnterNumber(Form f) {
        f.setScrollable(false);

        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        findLabel1(f).setText(" ");

        findHomeButton(f).setIcon(home_button);
        findHomeButton(f).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                showForm("Main", null);
            }
        });
        //findMobileHookButton(f).setIcon(mh_button);
        findContainer3(f).getStyle().setBgImage(bgwhite);
        findContainer3(f).getStyle().setBorder(null);
        findContainer(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Image bgImage = fetchResourceFile().getImage("WHITEBACKGROUND.png");
        f.getStyle().setBgImage(bgImage);
        f.getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
        //findBreakingNews(f).setIcon(newsImage.scaledWidth(Display.getInstance().getDisplayWidth() / 3));

        Command close = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);
                //Display.getInstance().exitApplication();
            }
        };


        f.getMenuBar().addCommand(close);
    }

    @Override
    protected void onEnterNumber_SaveNumberAction(Component c, ActionEvent event) {
        String number = findPhoneNumberTextArea(c.getComponentForm()).getText();

        if ("".equals(number)) {
            Dialog.show("Phone Number", "you have not entered phone number yet", "OK", null);
        } else {

            if (number.startsWith("0")) {
                number = number.substring(1);
            }

            if ("style".equals(sendingForm)) {
                subscribeMe("234" + number, aLife.getService());
                if ("200".equals(status)) {
                    if ("1".equals(result.getAsString("/response"))) {
                        Dialog.show("", "Successfuly subscribed", "OK", null);
                    } else {
                        Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                    }
                } else {
                    Dialog.show("", "you may not be connected to the internet", "OK", null);
                }

            }
            try {
                Storage.getInstance().writeObject("MyNumber", "234" + number);
                //Dialog.show("Saved", "Please try and subscribe again", "OK", null);
                myNumber = (String) Storage.getInstance().readObject("MyNumber");
                sendingForm = "";
                back();
            } catch (Exception e) {
                Dialog.show("Error", "could not save number " + "'" + e.getMessage() + "'", "OK", null);
            }
        }


    }

    @Override
    protected void onEnterNumber_CancelNumberAction(Component c, ActionEvent event) {
        back();
    }

    @Override
    protected void onVibeChatRegister_CountryComboBoxAction(Component c, ActionEvent event) {

        String country = (String) findCountryComboBox(c.getComponentForm()).getSelectedItem();
        if ("Nigeria".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+234");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(10);
        } else if ("Congo".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+242");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        } else if ("DRC".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+243");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        } else if ("Uganda".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+256");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        } else if ("Ghana".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+233");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        } else if ("Kenya".equals(country)) {
            findNumberExtension(c.getComponentForm()).setText("+254");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        } else if ("Zambia".equals(country)) {

            findNumberExtension(c.getComponentForm()).setText("+260");
            findPhoneNumberTextField(c.getComponentForm()).setMaxSize(9);
        }
        //System.out.println(country);
    }

    @Override
    protected void onVibeChatRegister_RegisterVibeAction(Component c, ActionEvent event) {
        // newNumber1 = null;
        final String nickname = findNickNameTextField(c.getComponentForm()).getText();
        final String sex = (String) findSexComboBox(c.getComponentForm()).getSelectedItem();
        final String age = findAgeTextField(c.getComponentForm()).getText();
        String country = (String) findCountryComboBox(c.getComponentForm()).getSelectedItem();
        String number = findPhoneNumberTextField(c.getComponentForm()).getText();
        //number.replace((number.charAt(0)), response);
        if (number.startsWith("0")) {
            number = number.substring(1);
        }
        final String newNumber1 = findNumberExtension(c.getComponentForm()).getText() + number;
        final String location = findLocationTextField(c.getComponentForm()).getText();
        //System.out.println(newNumber1);
        if (("".equals(number)) || ("".equals(nickname)) || ("".equals(age)) || ("".equals(location))) {
            findErrorMessage(c.getComponentForm()).setText("All fields are required");
            c.getComponentForm().revalidate();
            findNickNameTextField(c.getComponentForm()).requestFocus();
        } else {

            Command[] cmds = new Command[2];
            cmds[0] = new Command("OK") {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    subscribeVibeChat(nickname, newNumber1, age, sex, location);
                    if (("200".equals(status)) && (!("".equals(vibeResult)))) {
                        Dialog.show("", vibeResult.substring(4), "OK", null);
                    } else {
                        Dialog.show("", "you may not be connected to the internet", "OK", null);
                    }
                }
            };
            cmds[1] = new Command("Cancel") {
                @Override
                public void actionPerformed(ActionEvent evt) {
                }
            };


            TextArea area = new TextArea();
            //area.setUIID("customLabel2");
            area.setEditable(false);
            area.setText("Nickname : " + nickname + "\n"
                    + "Sex : " + sex + "\n"
                    + "Age : " + age + "\n"
                    + "Country : " + country + "\n"
                    + "Phone : " + newNumber1 + "\n"
                    + "Location : " + location);
            Dialog.show("Confirm", area, cmds);
        }
    }

    @Override
    protected void onVibeChatRegister_CancelRegisterAction(Component c, ActionEvent event) {

        back();
    }

    @Override
    protected void beforeFAQForm(Form f) {
        f.setScrollable(false);

//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        f.setTitle("FAQ");

//        findHomeButton(f).setIcon(home_button);
//        findHomeButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer1(f).getStyle().setBgImage(bgwhite);
//        findContainer1(f).getStyle().setBorder(null);
//        findContainer1(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Vector<Hashtable> v = new Vector<Hashtable>();

        Hashtable h1 = new Hashtable();
        h1.put("title", "What is VibeChat?");
        h1.put("content", "VibeChat is a pan African mobile chat service riding on the SMS delivery channel. "
                + "It is built to function across networks and tied to a central database. VibeChat is a low cost, "
                + "high subscriber service designed to enhance user experience with the network by creating a social community "
                + "across Africa via SMS Interactivity. ");
        v.add(h1);

        Hashtable h2 = new Hashtable();
        h2.put("title", "How do I sign-up?");
        h2.put("content", "To register, the user will simply SMS \"VIBECHATstrong\" to a preferred short code. A welcome message "
                + "with an embedded instruction for registration completion is returned to the user in his/her national language as well as in English. "
                + "To complete registration, user sends in a correct format his/her: \n"
                + "Nickname \n"
                + "Interest \n"
                + "Age \n"
                + "Sex \n"
                + "Example: Andre (space) 24 (space) F");
        v.add(h2);

        Hashtable h3 = new Hashtable();
        h3.put("title", "How do I chat?");
        h3.put("content", "To send a chat request, a registered user texts: \n"
                + "\"CHAT F\":To invite a female subscriber \n"
                + "\"CHAT M\": To invite a male subscriber \n"
                + "Once a chat request is sent, the user receives choices of available online contacts from which he/she can chose from to start chatting "
                + "\n"
                + "At the same point of request, chat invites are also sent to the choice contacts picked, notifying them that someone wants to chat with them. They may chose to chat or ignore the invite.\n"
                + "\n"
                + "To chat, user sends choice 'nickname' (space) message each time and at each reply ");

        v.add(h3);

        Hashtable h4 = new Hashtable();
        h4.put("title", "Can I chat with anyone on any network?");
        h4.put("content", "VibeChat is limited to the networks ascribed to the service. These are Warid, Yu, & Airtel more "
                + "networks are getting on board and notifications shall be made on their availability ");

        v.add(h4);

        Hashtable h5 = new Hashtable();
        h5.put("title", "How many people can I chat with at the same time?");
        h5.put("content", "Just like SMS, you can chat with as many people as possible. ");

        v.add(h5);

        Hashtable h6 = new Hashtable();
        h6.put("title", "Can we chat as a group?");
        h6.put("content", "Group Chat is currently not supported ");

        v.add(h6);

        Hashtable h7 = new Hashtable();
        h7.put("title", "Do I need to use a specific kind of phone?");
        h7.put("content", "Vibe Chat is an SMS service and so can be used on any kind of phone ");

        v.add(h7);

        Hashtable h8 = new Hashtable();
        h8.put("title", "How can I share my Username with others?");
        h8.put("content", "Tell your friends about the service and your username, if they "
                + "subscribe and send a message to your username, it will come to you! ");

        v.add(h8);

        Hashtable h9 = new Hashtable();
        h9.put("title", "How do I search for a number that I know?");
        h9.put("content", "You can search for a number that you know with the command Find (space) Friend's "
                + "Phone number. Example Find 242401929304 ");

        v.add(h9);

        Hashtable h10 = new Hashtable();
        h10.put("title", "What if I forget my username, can I change it?");
        h10.put("content", "You can change your username by sending Update (space) New Nickname (space) "
                + "New Age (space) New Sex Example: Update Andre 28 M ");

        v.add(h10);

        Hashtable h11 = new Hashtable();
        h11.put("title", "Can I go offline?");
        h11.put("content", "A user can go 'offline' at any point in time upon which he/she becomes unavailable to chat\n"
                + "\n"
                + "To come back online, the user is expected to text 'online' to the shortcode to come back online ");

        v.add(h11);

        Hashtable h12 = new Hashtable();
        h12.put("title", "Can I disable the service?");
        h12.put("content", "To completely unsubscribe from the VibeChat service, the user is expected to send 'deactivate' to the shortcode");

        v.add(h12);

        Hashtable h13 = new Hashtable();
        h13.put("title", "What happens when someone is unavailable or phone is off?");
        h13.put("content", "The messages to someone who's phone is off or is unavailable are queued, and will be pending depending on their network "
                + "settings & phone settings for message queues.");

        v.add(h13);

        Hashtable h14 = new Hashtable();
        h14.put("title", "What happens to messages sent to someone who is unsubscribed?");
        h14.put("content", "Messages to one who is unsubscribed do not reach the recipient but are terminated by the billing network.");

        v.add(h14);

        Hashtable h15 = new Hashtable();
        h15.put("title", "What is the maximum number of messages I can send per day?");
        h15.put("content", "There is no maximum number of messages, send as many messages as you would like.");

        Hashtable h16 = new Hashtable();
        h16.put("title", "Can I chat online from a computer?");
        h16.put("content", "VibeChat is currently a pure mobile SMS system, but if your phone can be linked to its own PC interface for messaging then this is possible.");

        v.add(h16);

        Hashtable h17 = new Hashtable();
        h17.put("title", "How does the service handle spam?");
        h17.put("content", "The service may from time to time send service updates, and targeted messages to subscribers in compliance with network policies against spamming.");

        v.add(h17);

        Hashtable h18 = new Hashtable();
        h18.put("title", "Can I \"trust\" or \"block\" a user by name or number?");
        h18.put("content", "You can control requests and chats by changing your status from online to offline, by sending off to the preferred shortcode, and back online by sending on to the shortcode.");

        v.add(h18);

        Hashtable h19 = new Hashtable();
        h19.put("title", "Does the network share or sell my chats with third parties?");
        h19.put("content", "Your chats on VibeChat are anonymous and we do not share your information with anyone. Network and national conditions & regulations regarding SMS apply.");

        v.add(h19);

        Hashtable h20 = new Hashtable();
        h20.put("title", "I am having trouble logging in. Who can help me?");
        h20.put("content", "At any point in time, a user can seek help on the process instructions by sending 'help' to the preferred shortcode\n"
                + "\n"
                + "One may also visit the Microsite to get information about the service and view terms and conditions. Customer care representatives from the network will also give you assistance.");

        v.add(h20);

        final List l = findFaqTitleMultiList(f);
        l.setModel(new DefaultListModel(v));

        l.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Hashtable h = (Hashtable) l.getSelectedItem(); //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //Dialog.show("", h.get("content").toString(), "OK", null);
//                Command[] cmds = new Command[1];
//                cmds[0] = new Command("OK") {
//                    @Override
//                    public void actionPerformed(ActionEvent evt) {
//                    }
//                };
//                TextArea area = new TextArea();
//                //area.setUIID("customLabel2");
//                area.setEditable(false);
//                area.setText(h.get("content").toString());
//                Dialog.show("FAQ", area, cmds);

                anFAQ = new FAQs(h.get("title").toString(), h.get("content").toString());
                showForm("EachFAQ", null);
            }
        });


        Command about = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);

            }
        };

        f.addCommand(about);
    }

    @Override
    protected void onMain_FaqAction(Component c, ActionEvent event) {

        showForm("FAQForm", null);
    }

    @Override
    protected void onMain_HollaBackAction(Component c, ActionEvent event) {

        showForm("hollaBackForm", null);
    }

    @Override
    protected void beforeHollaBackForm(Form f) {

        f.setScrollable(false);

//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        f.setTitle("Holla Back Form");

//        findHomeButton(f).setIcon(home_button);
//        findHomeButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer5(f).getStyle().setBgImage(bgwhite);
//        findContainer5(f).getStyle().setBorder(null);
//        findContainer5(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Command b = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showForm("Main", null);
            }
        };

        f.addCommand(b);//setBackCommand(b);


        if (Storage.getInstance().exists("MyNumber")) {
            myNumber = (String) Storage.getInstance().readObject("MyNumber");
            findHollaPhoneNumberTextField(f).setText(myNumber);
        } else {
            if (Display.getInstance().getMsisdn() != null) {
                myNumber = Display.getInstance().getMsisdn();
                findHollaPhoneNumberTextField(f).setText(myNumber);
            }
        }

//        if (!(myNumber == null)) {
//            if (myNumber.startsWith("0")) {
//                myNumber = myNumber.substring(1);
//            }

        // }

    }

    @Override
    protected void onHollaBackForm_SendHollaBackAction(Component c, ActionEvent event) {

        String name = findHollaNameTextField(c.getComponentForm()).getText();
        String phone = findHollaPhoneNumberTextField(c.getComponentForm()).getText();
        String location = findHollaLocationTextField(c.getComponentForm()).getText();
        String subject = findHollaSubjectTextField(c.getComponentForm()).getText();
        String msgText = findHollaMSGTextArea(c.getComponentForm()).getText();

        if (("".equals(subject)) || ("".equals(name)) || ("".equals(phone)) || ("".equals(location)) || ("".equals(msgText))) {
            findErrorMsgs(c.getComponentForm()).setText("All fields are required");
            c.getComponentForm().revalidate();
            findTextArea(c.getComponentForm()).requestFocus();
        } else {
            TextArea area = new TextArea();
            area.setText("Name : " + name + "\n"
                    + "Phone Number : " + phone + "\n"
                    + "Location : " + location + "\n"
                    + "Message Body : " + msgText);
            Message m = new Message(area.getText());
            //System.out.println(m.getContent());
            try {
                Display.getInstance().sendMessage(new String[]{("mobilehook@mtechcomm.com")}, subject, m);
                //Dialog.show("Done", "Your message has been sent", "OK", null);
            } catch (Exception e) {
                findErrorMsgs(c.getComponentForm()).setText(e.getMessage());
            }
        }


    }

    @Override
    protected void onHollaBackForm_CancelHollaAction(Component c, ActionEvent event) {

        back();
    }

    @Override
    protected void beforeVibeChatRegister(Form f) {

        f.setScrollable(false);

//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

//        findLabel6(f).setText(" ");
//        findHomeButton(f).setIcon(home_button);
//        findHomeButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer8(f).getStyle().setBgImage(bgwhite);
//        findContainer8(f).getStyle().setBorder(null);
//        findContainer8(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        if (Storage.getInstance().exists("MyNumber")) {
            myNumber = (String) Storage.getInstance().readObject("MyNumber");

        } else {
            if (Display.getInstance().getMsisdn() != null) {
                myNumber = Display.getInstance().getMsisdn();
            }
        }

        if (!(myNumber == null)) {
            if (myNumber.startsWith("0")) {
                myNumber = myNumber.substring(1);
            }
            findPhoneNumberTextField(f).setText(myNumber);
        }

        f.addCommand(new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                showForm("Main", null);
            }
        });
    }

    @Override
    protected void onAllTunes_NextTunesAction(Component c, ActionEvent event) {

        tuneBegining += 10;

        this.fetchTunes(String.valueOf(tuneBegining));
        if ("200".equals(status)) {
            if ((allTunes == null) || (allTunes.isEmpty())) {
                Dialog.show("oh!! dear", "Tunes were not fetched", "OK", null);
            } else {

                Container c2 = findAllTunesContainer(c.getComponentForm());
                c2.removeAll();

                for (int i = 0; i < allTunes.size(); i++) {
                    Hashtable h = allTunes.elementAt(i);
                    //System.out.println(h);
                    try {
                        c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                                h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                                h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
                    } catch (Exception e) {
                        TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                        t.setEditable(false);
                        t.setUIID("anodaText");
                        Dialog dlg = new Dialog("Please be Patient");
                        dlg.addComponent(t);
                        dlg.setTimeout(1000);
                        dlg.show();
                    }
                }

                pageNo += 1;
                findTunePageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));
                if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
                    //Dialog.show("Wait", "You are at the first page", "OK", null);
                    findPreviousTunes(c.getComponentForm()).setEnabled(false);
                } else {
                    findPreviousTunes(c.getComponentForm()).setEnabled(true);
                }

                c.getComponentForm().revalidate();//animateLayoutAndWait(5000);
            }

        } else {
            Dialog.show("oh Dear", "Could not fetch tunes", "OK", null);
        }
    }

    @Override
    protected void onAllTunes_PreviousTunesAction(Component c, ActionEvent event) {
        tuneBegining -= 10;

        this.fetchTunes(String.valueOf(tuneBegining));
        if ("200".equals(status)) {
            if ((allTunes == null) || (allTunes.isEmpty())) {
                Dialog.show("oh!! dear", "Tunes were not fetched", "OK", null);
            } else {

                Container c2 = findAllTunesContainer(c.getComponentForm());
                c2.removeAll();

                for (int i = 0; i < allTunes.size(); i++) {
                    Hashtable h = allTunes.elementAt(i);
                    //System.out.println(h);
                    try {
                        c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                                h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                                h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
                    } catch (Exception e) {
                        TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                        t.setEditable(false);
                        t.setUIID("anodaText");
                        Dialog dlg = new Dialog("Please be Patient");
                        dlg.addComponent(t);
                        dlg.setTimeout(1000);
                        dlg.show();
                    }
                }

                pageNo -= 1;
                findTunePageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));
                if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
                    //Dialog.show("Wait", "You are at the first page", "OK", null);
                    findPreviousTunes(c.getComponentForm()).setEnabled(false);
                } else {
                    findPreviousTunes(c.getComponentForm()).setEnabled(true);
                }

                c.getComponentForm().revalidate();//animateLayoutAndWait(5000);
            }

        } else {
            Dialog.show("oh Dear", "Could not fetch tunes", "OK", null);
        }

    }

    @Override
    protected void onAllTunes_SearchTuneAction(Component c, ActionEvent event) {

        String searchText = findTuneToSearchTextField(c.getComponentForm()).getText();
        if ("".equals(searchText)) {
            this.fetchTunes(String.valueOf(tuneBegining));

            if ("200".equals(status)) {
                //try {
                //tuneSize = allTopTunes.size();
                if ((allTunes == null) || (allTunes.size() <= 0)) {

                    Dialog.show("", "tunes were not fetched", "OK", null);
                } else {

                    Container c2 = findAllTunesContainer(c.getComponentForm());
                    c2.removeAll();
                    //tuneSize = allTunes.size();
                    for (int i = 0; i < allTunes.size(); i++) {
                        Hashtable h = allTunes.elementAt(i);
                        //System.out.println(h);
                        try {
                            c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                                    h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                                    h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
                        } catch (Exception e) {
                            TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                            t.setEditable(false);
                            t.setUIID("anodaText");
                            Dialog dlg = new Dialog("Please be Patient");
                            dlg.addComponent(t);
                            dlg.setTimeout(1000);
                            dlg.show();
                        }
                    }
//
//                    if (Display.getInstance().getCurrent() instanceof Dialog) {
//                        ((Dialog) Display.getInstance().getCurrent()).dispose();
//                    }
                    if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
                        //Dialog.show("Wait", "You are at the first page", "OK", null);
                        findPreviousTunes(c.getComponentForm()).setEnabled(false);
                    } else {
                        findPreviousTunes(c.getComponentForm()).setEnabled(true);
                    }

                    findTunePageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));

                    c.getComponentForm().revalidate();
                }
//                } catch (Exception e) {
//                    Dialog.show("", "connection may have been canelled", "OK", null);
//                }
            } else {
                Dialog.show("", "you may not be connected to the internet", "OK", null);
            }
        } else {
            this.searchTune(searchText);

            if ("200".equals(status)) {
                //try {
                //tuneSize = allTopTunes.size();
                if ((allTunes == null) || (allTunes.size() <= 0)) {
                    Dialog.show("", "search return nothing", "OK", null);
                } else {
                    Container c2 = findAllTunesContainer(c.getComponentForm());
                    c2.removeAll();
                    //tuneSize = allTunes.size();
                    for (int i = 0; i < allTunes.size(); i++) {
                        Hashtable h = allTunes.elementAt(i);
                        //System.out.println(h);
                        try {
                            c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
                                    h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
                                    h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
                        } catch (Exception e) {
                            TextArea t = new TextArea("An error has been encountered trying to display the tunes, please be patient");
                            t.setEditable(false);
                            t.setUIID("anodaText");
                            Dialog dlg = new Dialog("Please be Patient");
                            dlg.addComponent(t);
                            dlg.setTimeout(1000);
                            dlg.show();
                        }
                    }

                    if (((tuneBegining - 10) < 0) || ((pageNo - 1) < 1)) {
                        //Dialog.show("Wait", "You are at the first page", "OK", null);
                        findPreviousTunes(c.getComponentForm()).setEnabled(false);
                    } else {
                        findPreviousTunes(c.getComponentForm()).setEnabled(true);
                    }

                    findTunePageNumber(c.getComponentForm()).setText("Page " + String.valueOf(pageNo));

                    c.getComponentForm().revalidate();
                }
//                } catch (Exception e) {
//                    Dialog.show("", "connection may have been canelled", "OK", null);
//                }
            } else {
                Dialog.show("", "you may not be connected to the internet", "OK", null);
            }
        }
    }

//    @Override
//    protected void onAllTunes_TuneToSearchTextFieldAction(final Component c, ActionEvent event) {
//
//        findTuneToSearchTextField(c.getComponentForm()).addDataChangeListener(new DataChangedListener() {
//            public void dataChanged(int type, int index) {
//                String text = findTuneToSearchTextField(c.getComponentForm()).getText();
//                System.out.println("Type " + type);
//                System.out.println("Index " + index);
//                System.out.println("Lenght of Text : "+text.length());
//                searchTune(text);
//                
//                if ("200".equals(status)) {
//
//                    if ((allTunes == null) || (allTunes.size() <= 0)) {
//                        if (Display.getInstance().getCurrent() instanceof Dialog) {
//                            ((Dialog) Display.getInstance().getCurrent()).dispose();
//                        }
//                    } else {
//                        Container c2 = findAllTunesContainer(c.getComponentForm());
//                        c2.removeAll();
//                        tuneSize = allTunes.size();
//                        for (int i = 0; i < allTunes.size(); i++) {
//                            Hashtable h = allTunes.get(i);
//                            c2.addComponent(addTune(i, h.get("artist").toString(), h.get("artistimage").toString(), h.get("tune_name").toString(),
//                                    h.get("tune_id").toString(), h.get("mtn_code").toString(), h.get("glo_code").toString(),
//                                    h.get("etisalat_code").toString(), h.get("airtel_code").toString()));
//                        }
//                        if (Display.getInstance().getCurrent() instanceof Dialog) {
//                            ((Dialog) Display.getInstance().getCurrent()).dispose();
//                        }
//                        c.getComponentForm().revalidate();
//                    }
//
//                } else {
//                    Dialog.show("", "you may not be connected to the internet", "OK", null);
//                }
//
//            }
//        });
//    }
    @Override
    protected void onMain_VideosAction(Component c, ActionEvent event) {

        //        if ((allVideos == null) || (allVideos.isEmpty())) {
        //            this.fetchAllVideos();
        //            if ("200".equals(status)) {
        //                showForm("AllVideos", null);
        //            } else {
        //                Dialog.show("Yikes!!!", "you may not be connected to the internet", "OK", null);
        //            }
        //        } else {
        showForm("AllVideos", null);

        //        }

        // Dialog.show("Sorry", "coming soon", "OK", null);

    }

    @Override
    protected void beforeAllVideos(final Form f) {
        f.setScrollable(false);

        final WebBrowser wb = findWebBrowser(f);
        wb.setURL("http://mobile-hook.com/api/video_web.php");
        //wb.onLoad(status);

        findContainer(f).removeAll();
        findContainer(f).addComponent(BorderLayout.CENTER, wb);

        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        findLabel(f).setText(" ");
        findHomeButt(f).setIcon(home_button);
        findHomeButt(f).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                wb.setPage("<html><body></body></html>", "");
                wb.destroy();
                showForm("Main", null);
            }
        });
        //findMobileHookButton(f).setIcon(mh_button);
        findContainer1(f).getStyle().setBgImage(bgwhite);
        findContainer1(f).getStyle().setBorder(null);
        findContainer1(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);


//        Container c = findEmbeddedContainer(f);
//        c.removeAll();
//
//        for (int i = 0; i < allVideos.size(); i++) {
//            Hashtable h = allVideos.elementAt(i);
//            c.addComponent(BorderLayout.CENTER, addVideo((String) h.get("embed_url"), (String) h.get("video_title")));
//        }
        Command home = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                wb.setPage("<html><body></body></html>", "");
                wb.destroy();
                showForm("Main", null);
                //Collections.shuffle(allGists);
            }
        };
        f.addCommand(home);

        f.setBackCommand(new Command("Back") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                //f.getContentPane().forceRevalidate();//removeComponent(wb);
                wb.setPage("<html><body></body></html>", "");
                wb.destroy();
                back();
            }
        });
    }

    @Override
    protected void onNewsLifeStyle_JobsConnectAction(Component c, ActionEvent event) {

        service = "jobs";
        Command[] cmds = new Command[2];
        cmds[0] = new Command("Subscribe") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                myNumber = Display.getInstance().getMsisdn();
                if (myNumber == null) {
                    if (Storage.getInstance().exists("MyNumber")) {
                        myNumber = (String) Storage.getInstance().readObject("MyNumber");

                        subscribeMe(myNumber, service);
                        if ("200".equals(status)) {
                            if ("1".equals(result.getAsString("/response"))) {
                                Dialog.show("", "Successfuly subscribed", "OK", null);
                            } else {
                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                            }
                        } else {
                            Dialog.show("", "you may not be connected to the internet", "OK", null);
                        }

                    } else {
                        sendingForm = "style";
                        showForm("AddNumber", null);
                    }
                } else {
                    subscribeMe(myNumber, service);
                    if ("200".equals(status)) {
                        if ("1".equals(result.getAsString("/response"))) {
                            Dialog.show("", "Successfuly subscribed", "OK", null);
                        } else {
                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                        }
                    } else {
                        Dialog.show("", "you may not be connected to the internet", "OK", null);
                    }
                }
            }
        };
        cmds[1] = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("This service is designed to help you start a good professional career or "
                + "change your career choice and also for school leavers who are looking for a first Job. "
                + "Our Daily Job alerts brings you available latest Job openings as requested for by "
                + "you, to your phones so you do not miss out on anything that suits your career pursuit. ");
        //Dialog.show("Jobs Connect", area, cmds);

        aLife = new NewsAndLifeStyles("Jobs Connect", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_AccuWeatherAction(Component c, ActionEvent event) {

        service = "aw";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Being able to know what the weather would look like before setting for your daily "
                + "activities is very essential. This service provides you with daily weather updates "
                + "both locally and across the country so you dont get caught in the rain on your way to work, "
                + "meetings, traveling to a different town, e.t.c. ");
        //Dialog.show("Accu Weather", area, cmds);

        aLife = new NewsAndLifeStyles("Weather", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_EntertainmentGistAction(Component c, ActionEvent event) {

        service = "gist";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Stay up close and personal with your Local Stars and Celebs in the Entertainment "
                + "world right to your phone. This service provides you with local entertainment news alerts "
                + "round the clock to keep you up to date on happenings within their locality and nationwide. ");
        // Dialog.show("Entertainment Gist", area, cmds);

        aLife = new NewsAndLifeStyles("Entertainment Gist", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_DailyScripturesAction(Component c, ActionEvent event) {

        service = "bible";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Your spiritual life is the most essential of your being. This service is designed "
                + "to provide Daily Scriptures on Healing, Prosperity, Faith and Prayer. It is a service "
                + "designed to improve your spiritual life and draw you closer to the one who created you. "
                + "The service is expected to be automatically renewable after 30 days. ");
        // Dialog.show("Daily Scriptures", area, cmds);

        aLife = new NewsAndLifeStyles("Daily Scriptures", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_VibeChatAction(Component c, ActionEvent event) {

        service = "vibe";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                showForm("VibeChatRegister", null);
//
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("In this age of Social media, chats and SMS, it very important to always stay in "
                + "touch with friends, Classmates, groupies, e.t.c. VibeChat is an SMS service that enables "
                + "mobile subscribers interact on a Pan-African social networking platform and stay in touch "
                + "any where and anytime with your friends. ");
        //Dialog.show("Vibe Chat", area, cmds);

        aLife = new NewsAndLifeStyles("Vibe Chat", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected void onNewsLifeStyle_BreakingBadHabitAction(Component c, ActionEvent event) {
        //
        service = "Habit";
//        Command[] cmds = new Command[2];
//        cmds[0] = new Command("Subscribe") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//
//                myNumber = Display.getInstance().getMsisdn();
//                if (myNumber == null) {
//                    if (Storage.getInstance().exists("MyNumber")) {
//                        myNumber = (String) Storage.getInstance().readObject("MyNumber");
//
//                        subscribeMe(myNumber, service);
//                        if ("200".equals(status)) {
//                            if ("1".equals(result.getAsString("/response"))) {
//                                Dialog.show("", "Successfuly subscribed", "OK", null);
//                            } else {
//                                Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                            }
//                        } else {
//                            Dialog.show("", "you may not be connected to the internet", "OK", null);
//                        }
//
//                    } else {
//                        showForm("EnterNumber", null);
//                    }
//                } else {
//                    subscribeMe(myNumber, service);
//                    if ("200".equals(status)) {
//                        if ("1".equals(result.getAsString("/response"))) {
//                            Dialog.show("", "Successfuly subscribed", "OK", null);
//                        } else {
//                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
//                        }
//                    } else {
//                        Dialog.show("", "you may not be connected to the internet", "OK", null);
//                    }
//                }
//            }
//        };
//        cmds[1] = new Command("Cancel") {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//            }
//        };


        TextArea area = new TextArea();
        //area.setUIID("customLabel2");
        area.setEditable(false);
        area.setText("Available only to GLO and VISAFONE subscribers. \n"
                + "The right Habit can lead to endless success, and a bad habit can ruin successes built over the years. "
                + "This service is designed to provide you with tips on how to break bad habits and stay on course to succeed."
                + "This is biweekly service that has quality contents to help you overcome that bad habit.");
        //Dialog.show("Braking Bad habbits", area, cmds);

        aLife = new NewsAndLifeStyles("Braking Bad habbits", area.getText(), service);
        showForm("SelectedNewsAndLifeStyle", null);
    }

    @Override
    protected boolean processBackground(final Form f) {

        super.processBackground(f);
//        if ("Main".equals(f.getName())) {
//            if (Storage.getInstance().exists("AntmUser")) {
//
//                showForm("Menu", null);
//                return false;
//
//            }
//            //return true;//loadDataFromStorage();
//        }
        return true;
    }

    @Override
    protected void beforeSplashScreen(Form f) {

        Display.getInstance().lockOrientation(true);
        Image bgImage = fetchResourceFile().getImage("Splash_Screen.png");
        f.getStyle().setBgImage(bgImage);
        f.getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED);
    }

    @Override
    protected void beforeAbout(Form f) {
        f.setScrollable(false);
//        Image bggreen = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

//        findLabel(f).setText("Mtech Communications");
//        findHomeB(f).setIcon(home_button);
//        findHomeB(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer(f).getStyle().setBgImage(bggreen);
//        findContainer(f).getStyle().setBorder(null);
//        findContainer(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        Command about = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);

            }
        };

        f.addCommand(about);
    }

    @Override
    protected void beforeEachFAQ(Form f) {

        f.setScrollable(false);
//        Image bggreen = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        f.setTitle("FAQ");
//        findHomeButtons(f).setIcon(home_button);
//        findHomeButtons(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer2(f).getStyle().setBgImage(bggreen);
//        findContainer2(f).getStyle().setBorder(null);
//        findContainer2(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        findSelectedFAQText(f).setText(anFAQ.getContent());

        Command about = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {

                showForm("Main", null);

            }
        };

        f.addCommand(about);
    }

    @Override
    protected void onEachFAQ_FaqOKbuttonAction(Component c, ActionEvent event) {

        back();
    }

    @Override
    protected void onSelectedNewsAndLifeStyle_SubscribeButtonAction(Component c, ActionEvent event) {
        if ("vibe".equals(aLife.getService())) {
            showForm("VibeChatRegister", null);
        } else {

            myNumber = Display.getInstance().getMsisdn();
            if (myNumber == null) {
                if (Storage.getInstance().exists("MyNumber")) {
                    myNumber = (String) Storage.getInstance().readObject("MyNumber");

                    subscribeMe(myNumber, aLife.getService());
                    if (("200".equals(status)) && (!(result == null) || ("".equals(result)))) {
                        status = "";
                        if ("1".equals(result.getAsString("/response"))) {
                            Dialog.show("", "Successfuly subscribed", "OK", null);

                        } else {
                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                        }
                    } else {
                        Dialog.show("", "you may not be connected to the internet", "OK", null);
                    }

                } else {
                    sendingForm = "style";
                    showForm("AddNumber", null);
                }
            } else {
                subscribeMe(myNumber, aLife.getService());
                if (("200".equals(status)) && (!(result == null) || ("".equals(result)))) {
                    status = "";
                    if ("1".equals(result.getAsString("/response"))) {
                        Dialog.show("", "Successfuly subscribed", "OK", null);
                    } else {
                        Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                    }
                } else {
                    Dialog.show("", "you may not be connected to the internet", "OK", null);
                }
            }

        }
    }

    @Override
    protected void beforeSelectedNewsAndLifeStyle(Form f) {
        f.setScrollable(false);

//        Image bgwhite = fetchResourceFile().getImage("forgodwingreenline2.jpg");
//        Image home_button = fetchResourceFile().getImage("mobilehookfulllogo.png");
        //Image mh_button = fetchResourceFile().getImage("M_H_logo.png");

        f.setTitle(aLife.getTitle());

//        findButton(f).setIcon(home_button);
//        findButton(f).addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                showForm("Main", null);
//            }
//        });
//        //findMobileHookButton(f).setIcon(mh_button);
//        findContainer1(f).getStyle().setBgImage(bgwhite);
//        findContainer1(f).getStyle().setBorder(null);
//        findContainer1(f).getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED, true);

        findTextArea(f).setText(aLife.getContent());
        Command b = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showForm("Main", null);
            }
        };

        f.addCommand(b);//setBackCommand(b);
    }

    public Container addGist2(List l) {
        Resources res = fetchResourceFile();
        Container c = createContainer(res, "Gist2Renderer");

        //ImageDownloadService.createImageToStorage(gist_image, l, gist_id, new Dimension((Display.getInstance().getDisplayHeight() / 4), (Display.getInstance().getDisplayWidth() / 5)));
        return c;
    }

    private void addAvatar(List list, Hashtable gist, int i) {
        //        c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
//                                    h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
        Dimension postIconSize = new Dimension(Display.getInstance().getDisplayHeight() / 5, Display.getInstance().getDisplayHeight() / 4);
        String url = (String) gist.get("gist_image");
        String gistId = (String) gist.get("gist_id");
        Image im = (Image) gist.get("placeholder");
        if (url == null || url.startsWith("http:") == false) {
            // ImageDownloadService doesn't support HTTPS at moment
            return;
        }
        //System.out.println(gistId);
        //ImageDownloadService.createImageToStorage(picture, (List) list, index, "post_pic", (String)v.get("id"), postIconSize);
        ImageDownloadService.createImageToStorage(url, (List) list, i, "placeholder", gistId, postIconSize);

    }

//    @Override
//    protected void beforeGists2(Form f) {
//        //List l = findGistList(f);
//        //l.setModel(new DefaultListModel(allGists));
//
//        super.beforeGists2(f);//MainForm(f);
//        final List list = findGistList(f);
//        //list.setModel(new DefaultListModel(allGists));
//        Component selected = createContainer(fetchResourceFile(), "Gist2Renderer");
//        Component unselected = createContainer(fetchResourceFile(), "Gist2Renderer");
//        list.setRenderer(new GenericListCellRenderer(selected, unselected) {
//            @Override
//            public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
//                for (int i = 0; i < allGists.size(); i++) {
//                    Hashtable h = allGists.get(i);
//                    //list.addItem(h);
//                    addAvatar(list, h, i);
//                }
//                return super.getListCellRendererComponent(list, value, index, isSelected);
//            }
//        });
//
//
//    }
    @Override
    protected boolean initListModelGistList(List cmp) {
        super.initListModelGistList(cmp);//nitListModelList(cmp);
        cmp.setModel(new DefaultListModel(allGists));
        return true;
    }

//    @Override
//    protected void onGists2_GistListAction(Component c, ActionEvent event) {
//        List l = (List) c;
//        Hashtable h = (Hashtable) l.getSelectedItem();
//
////         c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
////                                    h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));
//
//        gist = new Gists(h.get("gist_image").toString(), h.get("gist_title").toString(), h.get("gist_id").toString(), h.get("gist_time_posted").toString(), StringUtil.replaceAll(h.get("gist_content").toString(), "'n", "\n"), h.get("shorturl").toString());
//        showForm("SelectedGist", null);
//
//        System.out.println("Selected Gist : " + h.toString());
//    }
    @Override
    protected void onAllGists_GistListAction(Component c, ActionEvent event) {
        List l = (List) c;
        Hashtable h = (Hashtable) l.getSelectedItem();

//         c2.addComponent(addGist(h.get("gist_title").toString(), h.get("gist_content").toString(), h.get("gist_time_posted").toString(),
//                                    h.get("gist_image").toString(), h.get("gist_id").toString(), h.get("shorturl").toString()));

        gist = new Gists(h.get("gist_image").toString(), h.get("gist_title").toString(), h.get("gist_id").toString(), h.get("gist_time_posted").toString(), StringUtil.replaceAll(h.get("gist_content").toString(), "'n", "\n"), h.get("shorturl").toString());
        showForm("SelectedGist", null);
    }

    @Override
    protected void onAddNumber_SaveNumberAction(Component c, ActionEvent event) {
        String number = findPhoneNumberTextField(c.getComponentForm()).getText();

        if ("".equals(number)) {
            ((Dialog) Display.getInstance().getCurrent()).dispose();
            Dialog.show("Phone Number", "you have not entered phone number yet", "OK", null);
        } else {
            if (!number.startsWith("0")) {
                ((Dialog) Display.getInstance().getCurrent()).dispose();
                Dialog.show("Phone Number", "your phone number did not start with '0'", "OK", null);
            } else {
                if (number.startsWith("0")) {
                    number = "234" + number.substring(1);
                }

                try {
                    Storage.getInstance().writeObject("MyNumber", number);
                    //Dialog.show("Saved", "Please try and subscribe again", "OK", null);
                    myNumber = (String) Storage.getInstance().readObject("MyNumber");
                    // sendingForm = "";
                    //((Dialog) Display.getInstance().getCurrent()).dispose();
                    //back();
                } catch (Exception e) {
                    Dialog.show("Error", "could not save number " + "'" + e.getMessage() + "'", "OK", null);
                }

                if ("style".equals(sendingForm)) {
                    subscribeMe(number, aLife.getService());
                    if ("200".equals(status)) {
                        if ("1".equals(result.getAsString("/response"))) {
                            ((Dialog) Display.getInstance().getCurrent()).dispose();
                            Dialog.show("", "Successfuly subscribed", "OK", null);
                        } else {
                            Dialog.show("", "An error occured trying to subscribe you", "OK", null);
                        }
                    } else {
                        Dialog.show("", "you may not be connected to the internet", "OK", null);
                    }

                } else {
                    ((Dialog) Display.getInstance().getCurrent()).dispose();
                    Dialog.show("Number", "your number has been stored, please select the tune again", "OK", null);
                }
            }


        }
    }

    @Override
    protected void onAddNumber_CancelAction(Component c, ActionEvent event) {
        ((Dialog) Display.getInstance().getCurrent()).dispose();
    }
}
//https://www.youtube.com/watch?v=WgTpVJa4urU
//https://www.youtube.com/watch?v=WJyEbqTSqoc
