package com.nrims.holder_data;

import java.io.*;
import com.nrims.common.*;
import java.util.ArrayList;
/**
 * Specific class to handle the file I/O for the holder coordinates file
 * The formatting assumes big-endian arrangement where applicable.
 * @author bepstein
 */
public class REFDataFile {
     /* private constants */
     /* describe file formating, mostly
     */
    private final int nRecordHeaderSizeLocation = 0;
    private final int nRecordHeaderSize = 16012;
    private final int nFileHeaderSizeLocation = 4;
    private final int nFileHeaderSize = 240;
    
    /* conversion factor from micrometers to milimeters */
    private final double coord_conv_factor = 0.001;

    /* This 240-byte array contains the buffer */
    private final byte[] holder_file_buffer_arr =
    {
        0x56, 0x32, 0x2E, 0x30, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x49, 0x4D, 0x53, 0x35, 0x46, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x55, 0x53, 0x52, 0x5F, 0x41, 0x4E, 0x41, 0x5F,
        0x52, 0x45, 0x46, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    private final int ibd_ref_size = 990;
    private final int int_size = 4;
    private final int double_size = 8;
    private final int ibd_max_ref = 200; /* maximum number of ref points in a list */
    private final int point_arr_num = 0; /* numero de pe */
    private final int idb_taille_com = 80; /* comment length */
    private final int idb_taille_dat = 20; /* date string length */

    /* struct entete_enr_structure copied */
    private final int nb_max_enr = 1;
    private final int nb_enr = 1;
    private final int taille_enr = 187208;
    private int[] tab_enr = new int[2000]; /* Ref list tab location */
    private int[] tab_trou = new int[2000]; /* unused */
    /* struct entete_enr_structure end */

    /* private variables and methods */
    private String in_file_path;
    private String out_file_path;
    private FileInputStream file_in;
    private FileOutputStream file_out;
    private ArrayList<REFPoint> rpl;

    private void setStreamsToNull() throws IOException{
        if ( file_in != null ){
            file_in.close();
            file_in = null;
        }

        if ( file_out != null ){
            file_out.close();
            file_out = null;
        }
    }

    private void setFilePathsToNull(){
        in_file_path = null;
        out_file_path = null;
    }

    /* sets arrays to zero value where appropriate,
     * initializes the RefPointList container,
     * closes I/O streams,
     * etc.
     */
    private void initClassData()
            throws IOException
    {
        int i;

        setStreamsToNull();
        rpl = new ArrayList<REFPoint>();
        
        //This is unnecessary. Strings are initialized to null by default:
        setFilePathsToNull();

        for (i = 0; i < tab_enr.length; i++)
            tab_enr[ i ] = 0;

        for (i = 0; i < tab_trou.length; i++)
            tab_trou[ i ] = 0;
    }

    private int readInFileHeader(FileInputStream fi)
    {
        int curr_offset = 0;
        int temp_int;
        
        /* stub code 
         * no reading done, just moving through to the first points array
         */
        byte[] temp_bytes = new byte[referenceListOffset(point_arr_num)];

        try {
            curr_offset += fi.read( temp_bytes);
            
        } catch (IOException ioe) {

        } finally {
            return( curr_offset );
        }
    }

    private int writeOutFileHeader
        (FileOutputStream fo
        ){
        int temp_i;
        double temp_d;
        int curr_offset = 0; /* keeps track of offset into the file */

        try{
           writeOutInt( fo, nRecordHeaderSize );
           writeOutInt( fo, nFileHeaderSize );
           curr_offset += 8;

           /* fill in the header */
           fo.write( holder_file_buffer_arr );
           curr_offset += holder_file_buffer_arr.length;

           writeOutInt( fo, nb_max_enr );
           writeOutInt( fo, nb_enr );
           writeOutInt( fo, taille_enr );
           curr_offset += 12;

           writeOutIntArr( fo, tab_enr);
           curr_offset += 4 * tab_enr.length;

           curr_offset += writeOutIntArr( fo, tab_trou );
           return(curr_offset);
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            return(curr_offset);
        }
    }

    private REFPoint readInRefPoint(FileInputStream fi)
            throws IOException
    {
        REFPoint ret_value = new REFPoint();
        byte[] temp_bytes;
        int[] temp_links;
        int i;

        /* Skipping the first 4 bytes. Recording them. */
        temp_bytes = new byte[4];
        fi.read( temp_bytes );
        ret_value.setBufferArr1( temp_bytes );

        /* Reading in the comment */
        temp_bytes = new byte[ idb_taille_com ];
        fi.read( temp_bytes );

        temp_bytes = DataUtilities.adjustAndNullTerminateByteArray(
            temp_bytes,
            idb_taille_com
            );

        ret_value.setComment( new String( temp_bytes ) );

        /* Reading in the date string */
        temp_bytes = new byte[ idb_taille_dat ];
        fi.read( temp_bytes );

        temp_bytes = DataUtilities.adjustAndNullTerminateByteArray(
            temp_bytes,
            idb_taille_dat
            );

        ret_value.setDateString( new String(temp_bytes) );

        /* Reading in the coordinates.
         * Converting them from milimeters to microns for further storage
         * and processing.
         */
        ret_value.setXCoord( readInDouble(fi) / coord_conv_factor );
        ret_value.setYCoord( readInDouble(fi) / coord_conv_factor );
        ret_value.setZCoord( readInDouble(fi) / coord_conv_factor );

        /* Skipping the 4 bytes. Recording them. */
        temp_bytes = new byte[4];
        fi.read( temp_bytes );
        ret_value.setBufferArr2( temp_bytes );

        /* Reading in and setting the number of links */
        ret_value.setNumberOfLinks( readInInt(fi) );

        /* Reading in the links arr*/
        temp_links = ret_value.getRefPointLinks();
        
        for (i = 0; i < temp_links.length; i++)
        {
            temp_links[i] = readInInt( fi );
        }

        ret_value.setRefPointLinks( temp_links );
        
        return( ret_value );
    }

    private int writeOutRefPoint(
            FileOutputStream fo,
            REFPoint rf)
    {
        int offset = 0;
        byte[] temp_bytes;
        int[] ref_point_links_arr;
        double temp_coord;

        try{
            /* Write a 4-byte buffer at the start; seems necessary */
            fo.write( rf.getBufferArr1() );
            offset += rf.getBufferArr1().length;

            temp_bytes = DataUtilities.adjustAndNullTerminateByteArray(
                    rf.getComment().getBytes(),
                    idb_taille_com
                    );

            fo.write( temp_bytes );
            offset += temp_bytes.length;


            temp_bytes = DataUtilities.adjustAndNullTerminateByteArray(
                    rf.getDateString().getBytes(),
                    idb_taille_dat
                    );

            fo.write( temp_bytes );
            offset += temp_bytes.length;

            temp_coord = coord_conv_factor * rf.getXCoord();
            writeOutDouble( fo, temp_coord );
            offset += 8;

            temp_coord = coord_conv_factor * rf.getYCoord();
            writeOutDouble( fo, temp_coord);
            offset += 8;

            temp_coord = coord_conv_factor * rf.getZCoord();
            writeOutDouble( fo, temp_coord );
            offset += 8;

            fo.write( rf.getBufferArr2() );
            offset += rf.getBufferArr2().length;

            writeOutInt( fo, rf.getNumberOfLinks() );
            offset += 4;

            offset += writeOutIntArr(  fo, rf.getRefPointLinks() );
        } catch (IOException ioe) {

        } finally {
            return( offset );
        }
    }

    private int readInInt(FileInputStream fi)
            throws IOException
    {
        byte[] temp_bytes = new byte[4];
        fi.read( temp_bytes );
        DataUtilities.reverseByteOrder( temp_bytes );
        return( DataUtilities.byte4ToInt( temp_bytes ) );
    }

    private void writeOutInt(FileOutputStream fo,
            int i) throws IOException
    {
        byte[] temp_bytes;
        temp_bytes = DataUtilities.intToByteArr(i);
        DataUtilities.reverseByteOrder(temp_bytes);
        fo.write(temp_bytes);
    }

    private double readInDouble(FileInputStream fi)
            throws IOException
    {
        byte[] byte_arr1 = new byte[4];
        byte[] byte_arr2 = new byte[4];
        byte[] byte_total = new byte[8];
        int i;

        fi.read( byte_arr1 );
        fi.read( byte_arr2 );

        DataUtilities.reverseByteOrder( byte_arr1 );
        DataUtilities.reverseByteOrder( byte_arr2 );

        for (i = 0; i < 4; i++)
            byte_total[i] = byte_arr1[i];

        for (i = 4; i < 8; i++)
            byte_total[i] = byte_arr2[i - 4];

        return( DataUtilities.byte8ToDouble( byte_total ) );
    }

    private void writeOutDouble(
           FileOutputStream fo,
           double d)
           throws IOException
    {
        byte[] temp_bytes;
        byte[] temp_bytes1 = new byte[4];
        byte[] temp_bytes2 = new byte[4];
        int i;

        /* Obtaining 8-byte array for double */
        temp_bytes = DataUtilities.doubleToByteArr(d);

        /* Breaking the double into 2 4-32-bit words */
        for (i = 0; i < 4; i++)
            temp_bytes1[i] = temp_bytes[i];

        for (i = 4; i < 8; i++)
            temp_bytes2[i - 4] = temp_bytes[i];

        /* Reversing bytes in each 4-byte word for the endian conversion */
        DataUtilities.reverseByteOrder(temp_bytes1);
        DataUtilities.reverseByteOrder(temp_bytes2);

        /* Writing the two 4-byte words out */
        fo.write(temp_bytes1);
        fo.write(temp_bytes2);
    }

    private int writeOutIntArr(
            FileOutputStream fo,
            int[] i_arr) throws IOException
    {
        int i;
        int offset = 0;

        for (i = 0; i < i_arr.length; i++)
        {
            writeOutInt(fo, i_arr[i]);
            offset += 4;
        }

        return( offset );
    }

    private int referenceListOffset(int num_point){
        return(8 + nFileHeaderSize + nRecordHeaderSize + tab_enr[num_point] * taille_enr);
    }

    /* constructors */
    
    //Usage search: No patterns found
    public REFDataFile()
    {
        try {
            initClassData();
        } catch (IOException ioe){

        }
    }

    //Usage search: No patterns found
    public REFDataFile(
            String fpath,
            Boolean open_for_write
        ){
        try{
            initClassData();

            if (open_for_write){
                setOutFilePath(fpath);
                file_out = new FileOutputStream( getOutFilePath() );
            } else {
                setInFilePath( fpath );
                 file_in = new FileInputStream( getInFilePath() );
            }
        } catch (Exception e){
            
        }
    }

    public REFDataFile(String fpath, Boolean open_for_write, ArrayList<REFPoint> rpl){
        try{
            initClassData();

            if (open_for_write){
                setOutFilePath(fpath);
                file_out = new FileOutputStream( getOutFilePath() );
            } else {
                setInFilePath( fpath );
                file_in = new FileInputStream( getInFilePath() );
            }
        } catch (Exception e){

        }

        setRefPointList( rpl );
    }

    //Usage search: no patterns found
    public REFDataFile(
            String in_fpath,
            String out_fpath,
            ArrayList<REFPoint> r_p_l
        ){
        try{
            initClassData();

            setOutFilePath(out_fpath);
            file_out = new FileOutputStream( getOutFilePath() );

            setInFilePath( in_fpath );
            file_in = new FileInputStream( getInFilePath() );

        } catch (Exception e){

        }

        setRefPointList( r_p_l );
    }
    /* public methods and variables */
 

    public void setInFilePath(String path){
        in_file_path = path;
    }

    public String getInFilePath(){
        return( in_file_path );
    }

    public void setOutFilePath(String path){
        out_file_path = path;
    }

    public String getOutFilePath(){
        return( out_file_path );
    }

    public void setRefPointList(ArrayList<REFPoint> rpl_in){
        rpl = rpl_in;
    }
    

    public ArrayList<REFPoint> getRefPointList(){
        return( rpl );
    }
    
    
    public String readFileIn(ArrayList<REFPoint> rpl_in,
            FileInputStream fis
            )
    {
        String output = new String();
        int num_points, i;

        /* Exiting if r_p_l is null */
        if ( rpl_in == null )
            return "Read file failed, reference point list is null.";

        /* Clearning the list */
        rpl_in.clear();

        try {
            /* Reading in the file header */
            readInFileHeader( fis);

            /* Reading in the number of ref points */
            num_points = readInInt( fis );

            for (i = 0; i < num_points; i++)
                rpl_in.add( readInRefPoint( fis ) );
            
        } catch ( IOException ioe ){
           output = ".ref File could not be read. IOException.";
        }
        
        return output;
    }

    public String readFileIn()
    {
        return readFileIn( getRefPointList(), file_in );
    }

    /**
     * Writes out ther file based upon the ref point list rfl
     */
    public void writeFileOut()
    {
        writeFileOut( getRefPointList(), file_out);
    }

    public void writeFileOut
        (ArrayList<REFPoint> rpl_out,
        FileOutputStream fos
        ){
        int offset;
        int point_ref_offset;
        int i;
        byte b = 0;

        try {
            /* writing the header */
            offset = writeOutFileHeader( fos );

            /* Seeing if we are far enough along in the file */
            point_ref_offset = referenceListOffset(point_arr_num);

            if (point_ref_offset > offset)
            {
                for (i = offset; i < point_ref_offset; i++)
                {
                    fos.write(b);
                }

                offset = point_ref_offset;
            }

            /* Writing out the number of points.
             */
            writeOutInt(fos, rpl_out.size());
            offset += 4;
            
            for (i = 0; i < rpl_out.size(); i++)
                offset += writeOutRefPoint(fos, rpl_out.get(i));

            /* filling the points arr with blank points up to ibd_max_ref */
            for (i = rpl_out.size(); i < ibd_max_ref; i++)
                offset += writeOutRefPoint(fos, new REFPoint());

            /* Writing out an 4-byte blank word at the end.*/
            i = 0;
            writeOutInt( fos, i );

        /**/
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void close()
    {
        try {
            setStreamsToNull();
        } catch (IOException ioe ) {
            ioe.printStackTrace();
        }
    }

}