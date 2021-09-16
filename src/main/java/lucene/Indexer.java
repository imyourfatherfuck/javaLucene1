package lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath));

        //create the indexer
        writer = new IndexWriter(indexDirectory,
                new StandardAnalyzer(Version.LUCENE_36), true,
                IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();

        //index file contents
        Field contentField = new Field(LuceneConstants.CONTENTS,
                new FileReader(file));
        //index file name
        Field fileNameField = new Field(LuceneConstants.FILE_NAME,
                file.getName(),
                Field.Store.YES, Field.Index.NOT_ANALYZED);
        //index file path
        Field filePathField = new Field(LuceneConstants.FILE_PATH,
                file.getCanonicalPath(),
                Field.Store.YES, Field.Index.NOT_ANALYZED);

        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
    }

    private void indexFile(File file) throws IOException {
        System.out.println("读取 " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    public int createIndex(String dataDirPath, FileFilter filter)
            throws IOException {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();
//
//	      for (File file : files) {
//	         if(!file.isDirectory()
//	            && !file.isHidden()
//	            && file.exists()
//	            && file.canRead()
//	            && filter.accept(file)
//	         ){
//	            indexFile(file);
//	         }
//	      }


        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() && !files[i].isHidden() && files[i].exists()) {
                List allPath = getFile(files[i]);
                for (int j = 0; j < allPath.size(); j++) {
                    indexFile((File) allPath.get(j));
                }
            } else if (!files[i].isDirectory() && !files[i].isHidden() && files[i].exists()) {
                indexFile(files[i]);
            }
        }
        return writer.numDocs();
    }

    public static List getFile(File file) {
        List listLocal = new ArrayList();
        if (file != null) {
            File[] f = file.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    getFile(f[i]);
                    listLocal.add(f[i]);
                }
            } else {
            }
        }
        return listLocal;
    }

}
