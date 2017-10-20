package taewon.navercorp.integratedsns.model.comment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 2017-10-20.
 */

public class YoutubeComment {

    @SerializedName("snippet")
    private Snippet snippet = new Snippet();

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public class Snippet {

        @SerializedName("topLevelComment")
        private TopLevelComment topLevelComment = new TopLevelComment();
        @SerializedName("videoId")
        private String videoId;

        public TopLevelComment getTopLevelComment() {
            return topLevelComment;
        }

        public void setTopLevelComment(TopLevelComment topLevelComment) {
            this.topLevelComment = topLevelComment;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public class TopLevelComment {

            @SerializedName("snippet")
            private Comment snippet = new Comment();

            public Comment getSnippet() {
                return snippet;
            }

            public void setSnippet(Comment snippet) {
                this.snippet = snippet;
            }

            public class Comment {

                @SerializedName("textOriginal")
                private String textOriginal;

                public String getTextOriginal() {
                    return textOriginal;
                }

                public void setTextOriginal(String textOriginal) {
                    this.textOriginal = textOriginal;
                }

            }

        }

    }

}
