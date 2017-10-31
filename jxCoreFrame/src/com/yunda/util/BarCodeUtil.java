package com.yunda.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.MimeTypes;
import org.krysalis.barcode4j.tools.UnitConv;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class BarCodeUtil extends HttpServlet {
	private static final long serialVersionUID = -1612710758060435089L;

	/** Parameter name for the message */
	public static final String BARCODE_MSG = "msg";

	/** Parameter name for the dimType */
	public static final String BARCODE_DIMTYPE = "dimType"; // 1D或2D

	/** Parameter name for the barcode type */
	public static final String BARCODE_TYPE = "type";

	/** Parameter name for the barcode height */
	public static final String BARCODE_HEIGHT = "height";

	/** Parameter name for the module width */
	public static final String BARCODE_MODULE_WIDTH = "mw";

	/** Parameter name for the wide factor */
	public static final String BARCODE_WIDE_FACTOR = "wf";

	/** Parameter name for the quiet zone */
	public static final String BARCODE_QUIET_ZONE = "qz";

	/** Parameter name for the human-readable placement */
	public static final String BARCODE_HUMAN_READABLE_POS = "hrp";

	/** Parameter name for the output format */
	public static final String BARCODE_FORMAT = "fmt";

	/** Parameter name for the image resolution (for bitmaps) */
	public static final String BARCODE_IMAGE_RESOLUTION = "res";

	/** Parameter name for the grayscale or b/w image (for bitmaps) */
	public static final String BARCODE_IMAGE_GRAYSCALE = "gray";

	/** Parameter name for the font size of the human readable display */
	public static final String BARCODE_HUMAN_READABLE_SIZE = "hrsize";

	/** Parameter name for the font name of the human readable display */
	public static final String BARCODE_HUMAN_READABLE_FONT = "hrfont";

	/** Parameter name for the pattern to format the human readable message */
	public static final String BARCODE_HUMAN_READABLE_PATTERN = "hrpattern";

	private transient Logger log = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest,
	 *      HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String msg, dimType, format;
		dimType = request.getParameter(BARCODE_DIMTYPE);
		msg = request.getParameter(BARCODE_MSG);

		if (msg == null)msg = "0123456789";
		final int dpi = 200;
		format = MimeTypes.MIME_JPEG;
		if ("1D".equals(dimType)) { // 普通条形码
			try {
				int orientation = 0;
				Configuration cfg = buildCfg(request);
				BarcodeUtil util = BarcodeUtil.getInstance();
				BarcodeGenerator gen = util.createBarcodeGenerator(cfg);

				ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
				try {
					String gray = request.getParameter(BARCODE_IMAGE_GRAYSCALE);
					BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray) ? 
						new BitmapCanvasProvider(bout, format, dpi, BufferedImage.TYPE_BYTE_GRAY,true, orientation) : new BitmapCanvasProvider(bout,
							format, dpi, BufferedImage.TYPE_BYTE_BINARY, false,orientation));
					gen.generateBarcode(bitmap, msg);
					bitmap.finish();
				}catch(Exception e){
					e.printStackTrace();
				} finally {
					bout.close();
				}
				response.setContentType(format);
				response.setContentLength(bout.size());
				response.getOutputStream().write(bout.toByteArray());
				response.getOutputStream().flush();
			} catch (Exception e) {
				log.error("Error while generating barcode", e);
				throw new ServletException(e);
			} catch (Throwable t) {
				log.error("Error while generating barcode", t);
				throw new ServletException(t);
			}
		} else { // 2D条形码
			String[] paramArr = new String[] { msg };
			DataMatrixBean bean = new DataMatrixBean();

			// Configure the barcode generator
			bean.setModuleWidth(UnitConv.in2mm(8.0f / dpi)); // makes a
			// dot/module
			// exactly eight
			// pixels
			bean.doQuietZone(false);
			bean.setShape(SymbolShapeHint.FORCE_RECTANGLE);

			boolean antiAlias = false;
			int orientation = 0;
			// Set up the canvas provider to create a monochrome bitmap
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

			// Generate the barcode
			bean.generateBarcode(canvas, msg);

			// Signal end of generation
			canvas.finish();

			// Get generated bitmap
			BufferedImage symbol = canvas.getBufferedImage();
			
			int fontSize = 26; // pixels
			//int lineHeight = fontSize * 1.2;
			int lineHeight = 20;
			Font font = new Font("Arial", Font.PLAIN, fontSize);
			int width = symbol.getWidth();
			int height = symbol.getHeight();
			FontRenderContext frc = new FontRenderContext(new AffineTransform(), antiAlias, true);
			for (int i = 0; i < paramArr.length; i++) {
				String line = paramArr[i];
				Rectangle2D bounds = font.getStringBounds(line, frc);
				width = (int) Math.ceil(Math.max(width, bounds.getWidth()));
				height += lineHeight;
			}

			// Add padding
			int padding = 2;
			width += 2 * padding;
			height += 3 * padding;

			BufferedImage bitmap = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g2d = (Graphics2D) bitmap.getGraphics();
			g2d.setBackground(Color.white);
			g2d.setColor(Color.black);
			g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			g2d.setFont(font);

			// Place the barcode symbol
			AffineTransform symbolPlacement = new AffineTransform();
			symbolPlacement.translate(padding, padding);
			g2d.drawRenderedImage(symbol, symbolPlacement);

			// Add text lines (or anything else you might want to add)
			int y = padding + symbol.getHeight() + padding;
			for (int i = 0; i < paramArr.length; i++) {
				String line = paramArr[i];
				y += lineHeight;
				g2d.drawString(line, padding, y);
			}
			g2d.dispose();

			// Encode bitmap as file
			String mime = MimeTypes.MIME_JPEG;
			String outputFile = request.getSession().getServletContext().getRealPath("/out123.jpg");
			
			OutputStream out = new FileOutputStream(outputFile);
			try {
				final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
				encoder.encode(bitmap, out, mime, dpi);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.close();
			}
			//System.out.println("filePath = " + outputFile);
			response.setContentType(mime);
			//	得到输出流
			OutputStream output = response.getOutputStream();
			//	得到图片的文件流
			InputStream imageIn = new FileInputStream(new File(outputFile));
			//	得到输入的编码器，将文件流进行jpg格式编码
			JPEGImageDecoder jpegID = JPEGCodec.createJPEGDecoder(imageIn);
			//	得到编码后的图片对象
			BufferedImage image = jpegID.decodeAsBufferedImage();
			//	得到输出的编码器
			JPEGImageEncoder encoder1 = JPEGCodec.createJPEGEncoder(output);
			encoder1.encode(image);		// 对图片进行输出编码
			imageIn.close();			// 关闭文件流
		}
	}
	/**
	 * Build an Avalon Configuration object from the request.
	 * 
	 * @param request
	 *            the request to use
	 * @return the newly built COnfiguration object
	 * @todo Change to bean API
	 */
	protected Configuration buildCfg(HttpServletRequest request) {
		DefaultConfiguration cfg = new DefaultConfiguration("barcode");
		// Get type
		String type = request.getParameter(BARCODE_TYPE);
		if (type == null) {
			type = "code128";
		}
		DefaultConfiguration child = new DefaultConfiguration(type);
		cfg.addChild(child);
		// Get additional attributes
		DefaultConfiguration attr;
		String height = request.getParameter(BARCODE_HEIGHT);
		if (height != null) {
			attr = new DefaultConfiguration("height");
			attr.setValue(height);
			child.addChild(attr);
		}
		String moduleWidth = request.getParameter(BARCODE_MODULE_WIDTH);
		if (moduleWidth != null) {
			attr = new DefaultConfiguration("module-width");
			attr.setValue(moduleWidth);
			child.addChild(attr);
		}
		String wideFactor = request.getParameter(BARCODE_WIDE_FACTOR);
		if (wideFactor != null) {
			attr = new DefaultConfiguration("wide-factor");
			attr.setValue(wideFactor);
			child.addChild(attr);
		}
		String quietZone = request.getParameter(BARCODE_QUIET_ZONE);
		if (quietZone != null) {
			attr = new DefaultConfiguration("quiet-zone");
			if (quietZone.startsWith("disable")) {
				attr.setAttribute("enabled", "false");
			} else {
				attr.setValue(quietZone);
			}
			child.addChild(attr);
		}

		String humanReadablePosition = request
				.getParameter(BARCODE_HUMAN_READABLE_POS);
		String pattern = request.getParameter(BARCODE_HUMAN_READABLE_PATTERN);
		String humanReadableSize = request
				.getParameter(BARCODE_HUMAN_READABLE_SIZE);
		String humanReadableFont = request
				.getParameter(BARCODE_HUMAN_READABLE_FONT);

		if (!((humanReadablePosition == null) && (pattern == null)
				&& (humanReadableSize == null) && (humanReadableFont == null))) {
			attr = new DefaultConfiguration("human-readable");

			DefaultConfiguration subAttr;
			if (pattern != null) {
				subAttr = new DefaultConfiguration("pattern");
				subAttr.setValue(pattern);
				attr.addChild(subAttr);
			}
			if (humanReadableSize != null) {
				subAttr = new DefaultConfiguration("font-size");
				subAttr.setValue(humanReadableSize);
				attr.addChild(subAttr);
			}
			if (humanReadableFont != null) {
				subAttr = new DefaultConfiguration("font-name");
				subAttr.setValue(humanReadableFont);
				attr.addChild(subAttr);
			}
			if (humanReadablePosition != null) {
				subAttr = new DefaultConfiguration("placement");
				subAttr.setValue(humanReadablePosition);
				attr.addChild(subAttr);
			}
			child.addChild(attr);
		}
		return cfg;
	}
}
