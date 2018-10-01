package vn.toancauxanh.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.zkoss.zul.Filedownload;

public class ReadWord {
	@SuppressWarnings("resource")
	public static void readWord() {
		try {
			FileInputStream fis = new FileInputStream("C:\\demo\\gioi_thieu_ve_ban_than.docx");
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));

			// XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
			// XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
			//
			// XWPFHeader header = policy.getDefaultHeader();
			// if (header != null) {
			// System.out.println(header.getText());
			// }
			//
			// XWPFFooter footer = policy.getDefaultFooter();
			// if (footer != null) {
			// System.out.println(footer.getText());
			// }
			//
			// System.out.println(extractor.getText());

			List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

			for (XWPFParagraph paragraph : paragraphList) {
				if (paragraph.getText().contains("${name}")) {
					paragraph.getText().replace("${name}", "Nguyen Win");
					try (XWPFDocument doc = new XWPFDocument()) {

						XWPFParagraph p1 = doc.createParagraph();
						p1.setAlignment(ParagraphAlignment.CENTER);
						p1.setBorderBottom(Borders.DOUBLE);
						p1.setBorderTop(Borders.DOUBLE);

						p1.setBorderRight(Borders.DOUBLE);
						p1.setBorderLeft(Borders.DOUBLE);
						p1.setBorderBetween(Borders.SINGLE);

						p1.setVerticalAlignment(TextAlignment.TOP);

						XWPFParagraph p2 = doc.createParagraph();
						p2.setAlignment(ParagraphAlignment.LEFT);

						XWPFRun r5 = p2.createRun();
						r5.setText(paragraph.getText());
						r5.setFontSize(14);
						r5.addBreak();
						r5.addBreak();

						XWPFParagraph p3 = doc.createParagraph();
						p3.setWordWrapped(true);
						p3.setPageBreak(true);

						// p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
						p3.setAlignment(ParagraphAlignment.BOTH);
						// p3.setSpacingBetween(15, LineSpacingRule.EXACT);

						p3.setIndentationFirstLine(600);

						ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
						doc.write(fileOut);
						Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()), "application/octet-stream",
								"xx" + ".docx");
					}

				}

				System.out.println(paragraph.getText().replace("${name}", "Nguyen Win"));

			
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream("C:\\demo\\gioi_thieu_ve_ban_than.docx");
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));

			// XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
			// XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
			//
			// XWPFHeader header = policy.getDefaultHeader();
			// if (header != null) {
			// System.out.println(header.getText());
			// }
			//
			// XWPFFooter footer = policy.getDefaultFooter();
			// if (footer != null) {
			// System.out.println(footer.getText());
			// }
			//
			// System.out.println(extractor.getText());

			// List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
			//
			// for (XWPFParagraph paragraph : paragraphList) {
			// if (paragraph.getText().contains("${name}")) {
			// paragraph.getText().replace("${name}", "Nguyen Win");
			// }
			//
			// System.out.println(paragraph.getText().replace("${name}", "Nguyen Win"));
			//
			// }

			try (XWPFDocument doc = new XWPFDocument()) {

				XWPFParagraph p2 = doc.createParagraph();
				p2.setAlignment(ParagraphAlignment.LEFT);

				List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

				for (XWPFParagraph paragraph : paragraphList) {
					if (paragraph.getText().contains("${name}")) {
						paragraph.getText().replace("${name}", "Nguyen Win");
					}

					XWPFRun r5 = p2.createRun();
					r5.setText(paragraph.getText());
					r5.setFontSize(14);
					r5.addBreak();
					r5.addBreak();

					System.out.println(paragraph.getText().replace("${name}", "Nguyen Win"));

				}

				ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
				doc.write(fileOut);
				Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()), "application/octet-stream",
						"xx" + ".docx");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	
}
