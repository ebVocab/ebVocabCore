package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ProjectInfo;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.tools.StringReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class CloudAccessApache implements CloudAccess {
    private static final Logger logger = Logger.getLogger(CloudAccessApache.class.getName());

    public CloudResult doFetchProjectList(CloudAccessParameters accessParameters) throws Exception {
        CloudResult cloudResult = new CloudResult();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost projectListPost = new HttpPost(CloudUrl.getProjectListUrl(accessParameters.serverUrl));

        try {
            // Add your data
            List<NameValuePair> projectListParams = new ArrayList<NameValuePair>();
            projectListParams.add(new BasicNameValuePair(USER_NAME_PARAM, accessParameters.userName));
            projectListParams.add(new BasicNameValuePair(SECRET_PARAM, accessParameters.secret));
            projectListPost.setEntity(new UrlEncodedFormEntity(projectListParams));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(projectListPost);

            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return cloudResult;

            cloudResult.getRemoteResult().remoteProjectListResult(
                    new ProjectList(
                            StringReader.readString(
                                    response.getEntity().getContent(),
                                    UTF_8
                            )
                    )
            );

            return cloudResult;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }

    private File localFile(ProjectInfo projectInfo, FileItem fileItem) {
        File subDir = new File(
                projectInfo.getVocabDirectory(),
                fileItem.getRelativePath()
        );
        return new File(subDir, fileItem.getFileName());
    }

    public boolean doDownloadFile(
            CloudAccessParameters accessParameters,
            ProjectInfo projectInfo,
            FileItem item,
            CloudResult cloudResult) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            File localFile = localFile(projectInfo, item);
            File targetSubDir = localFile.getParentFile();
            if (!targetSubDir.exists())
                if (!targetSubDir.mkdirs()) {
                    cloudResult.getLocalResult().createError(targetSubDir);
                    return false;
                }

            HttpPost getFilePost = new HttpPost(CloudUrl.getGetFileUrl(accessParameters.serverUrl));
            List<NameValuePair> getFileParams = new ArrayList<NameValuePair>();
            getFileParams.add(new BasicNameValuePair(USER_NAME_PARAM, accessParameters.userName));
            getFileParams.add(new BasicNameValuePair(SECRET_PARAM, accessParameters.secret));
            getFileParams.add(new BasicNameValuePair(PROJECT_NAME_PARAM, projectInfo.getName()));
            getFileParams.add(new BasicNameValuePair(RELATIVE_PATH_PARAM, item.getRelativePath()));
            getFileParams.add(new BasicNameValuePair(FILE_NAME_PARAM, item.getFileName()));
            getFilePost.setEntity(new UrlEncodedFormEntity(getFileParams));

            HttpResponse response = httpClient.execute(getFilePost);
            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return false;

            InputStream in = response.getEntity().getContent();
            try {
                FileOutputStream outputStream =
                        new FileOutputStream(
                                localFile
                        );
                try {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }

                } finally {
                    outputStream.close();
                }
            } finally {
                in.close();
            }
            logger.info("Downloaded " + localFile.getAbsolutePath());
            cloudResult.getRemoteResult().downloadedOne();
            return true;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }

    public boolean doFetchFileListFromServer(CloudAccessParameters accessParameters, ProjectInfo projectInfo, CloudResult cloudResult) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost fileListPost = new HttpPost(CloudUrl.getFileListUrl(accessParameters.serverUrl));

        try {
            // Add your data
            List<NameValuePair> fileListParams = new ArrayList<NameValuePair>();
            fileListParams.add(new BasicNameValuePair(USER_NAME_PARAM, accessParameters.userName));
            fileListParams.add(new BasicNameValuePair(SECRET_PARAM, accessParameters.secret));
            fileListParams.add(new BasicNameValuePair(PROJECT_NAME_PARAM, projectInfo.getName()));
            fileListPost.setEntity(new UrlEncodedFormEntity(fileListParams));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(fileListPost);
            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return false;

            cloudResult.getRemoteResult().remoteFileListResult(
                    new FileList(
                            StringReader.readString(
                                    response.getEntity().getContent(),
                                    UTF_8
                            )
                    )
            );

            return true;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }

    public boolean doPostFile(
            CloudAccessParameters accessParameters,
            File file,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item,
            String locale
    ) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost = new HttpPost(CloudUrl.getFilePostUrl(accessParameters.serverUrl));
            // Add your data
            List<NameValuePair> filePostParams = new ArrayList<NameValuePair>();
            filePostParams.add(new BasicNameValuePair(USER_NAME_PARAM, accessParameters.userName));
            filePostParams.add(new BasicNameValuePair(SECRET_PARAM, accessParameters.secret));
            filePostParams.add(new BasicNameValuePair(PROJECT_NAME_PARAM, projectInfo.getName()));
            filePostParams.add(new BasicNameValuePair(RELATIVE_PATH_PARAM, item.getRelativePath()));
            filePostParams.add(new BasicNameValuePair(LOCALE_PARAM, locale));
            filePostParams.add(new BasicNameValuePair(LAST_MODIFIED_PARAM, String.valueOf(item.getLastModified())));
            filePostParams.add(new BasicNameValuePair(FILE_CONTENT_PARAM, VocabIOHelper.asString(file)));
            filePostParams.add(new BasicNameValuePair(FILE_NAME_PARAM, item.getFileName()));

            httpPost.setEntity(new UrlEncodedFormEntity(filePostParams));

            logger.info("executing request " + httpPost.getRequestLine());
            HttpResponse response = httpClient.execute(httpPost);
            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return false;

            logger.info(response.getStatusLine().toString());

            cloudResult.getRemoteResult().uploadedOne();
            if (Config.projectInitialized())
                CloudModel.getOrCreateCloudModel().uploadFinished();
            logger.info("Uploaded " + file.getCanonicalPath());
            return true;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }

    }

    public boolean doUploadFile(
            CloudAccessParameters accessParameters,
            File file,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item,
            String locale) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost = new HttpPost(CloudUrl.getFileUploadUrl(accessParameters.serverUrl));


            FileBody fileBody = new FileBody(file);
            final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();


            reqEntity.addPart("file", fileBody);

            reqEntity.addPart(USER_NAME_PARAM, new StringBody(accessParameters.userName, ContentType.TEXT_PLAIN));
            reqEntity.addPart(SECRET_PARAM, new StringBody(accessParameters.secret, ContentType.TEXT_PLAIN));
            reqEntity.addPart(PROJECT_NAME_PARAM, new StringBody(projectInfo.getName(), ContentType.TEXT_PLAIN));
            reqEntity.addPart(RELATIVE_PATH_PARAM, new StringBody(item.getRelativePath(), ContentType.TEXT_PLAIN));
            reqEntity.addPart(LOCALE_PARAM, new StringBody(locale, ContentType.TEXT_PLAIN));
            reqEntity.addPart(LAST_MODIFIED_PARAM, new StringBody(String.valueOf(item.getLastModified()), ContentType.TEXT_PLAIN));

            httpPost.setEntity(reqEntity.build());

            logger.info("executing request " + httpPost.getRequestLine());
            HttpResponse response = httpClient.execute(httpPost);
            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return false;

            HttpEntity resEntity = response.getEntity();

            logger.info(response.getStatusLine().toString());
            if (resEntity != null) {
                logger.info("Response content length: " + resEntity.getContentLength());
                logger.info(StringReader.readString(
                        resEntity.getContent(),
                        UTF_8
                ));
            }

            EntityUtils.consume(resEntity);

            cloudResult.getRemoteResult().uploadedOne();
            CloudModel.getOrCreateCloudModel().uploadFinished();
            logger.info("Uploaded " + file.getCanonicalPath());

            return true;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }

    public boolean doRemoveRemoteFile(
            CloudAccessParameters accessParameters,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost = new HttpPost(CloudUrl.getDeleteFileUrl(accessParameters.serverUrl));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(USER_NAME_PARAM, accessParameters.userName));
            nameValuePairs.add(new BasicNameValuePair(SECRET_PARAM, accessParameters.secret));
            nameValuePairs.add(new BasicNameValuePair(PROJECT_NAME_PARAM, projectInfo.getName()));
            nameValuePairs.add(new BasicNameValuePair(RELATIVE_PATH_PARAM, item.getRelativePath()));
            nameValuePairs.add(new BasicNameValuePair(FILE_NAME_PARAM, item.getFileName()));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, UTF_8));

            HttpResponse response = httpClient.execute(httpPost);
            if (!cloudResult.getRemoteResult().evaluateHTTPResponse(response.getStatusLine().getStatusCode()))
                return false;

            HttpEntity resEntity = response.getEntity();

            logger.info(response.getStatusLine().toString());
            if (resEntity != null) {
                logger.info("Response content length: " + resEntity.getContentLength());
                logger.info(StringReader.readString(
                        resEntity.getContent(),
                        UTF_8
                ));
            }

            EntityUtils.consume(resEntity);

            logger.info("Deleted file");
            cloudResult.getRemoteResult().deletedOne();

            return true;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }


}
