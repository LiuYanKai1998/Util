package ext.wisplm.util.sign;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DirectColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImageConvert {
	
	BufferedImage image;
    private int iw, ih;
    private int[] pixels;

    public ImageConvert(BufferedImage image) {
        this.image = image;
        iw = image.getWidth();
        ih = image.getHeight();
        pixels = new int[iw * ih];
    }
    
    /**
     * 图像二值化
     */
    public BufferedImage changeGrey() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设定二值化的域值，默认值为100
        int grey = 100;
        // 对图像进行二值化处理，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 0; i < iw * ih; i++) {
            int red, green, blue;
            int alpha = cm.getAlpha(pixels[i]);
            if (cm.getRed(pixels[i]) > grey) {
                red = 255;
            } else {
                red = 0;
            }
            if (cm.getGreen(pixels[i]) > grey) {
                green = 255;
            } else {
                green = 0;
            }
            if (cm.getBlue(pixels[i]) > grey) {
                blue = 255;
            } else {
                blue = 0;
            }
            pixels[i] = alpha << 24 | red << 16 | green << 8 | blue; // 通过移位重新构成某一点像素的RGB值
        }
        // 将数组中的象素产生一个图像
        Image tempImg = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(iw, ih, pixels, 0, iw));
        image = new BufferedImage(tempImg.getWidth(null),
                tempImg.getHeight(null), BufferedImage.TYPE_INT_BGR);
        image.createGraphics().drawImage(tempImg, 0, 0, null);
        return image;
    }

    /**
     * 中值滤波
     */
    public BufferedImage getMedian() {
        PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih,pixels, 0, iw);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 对图像进行中值滤波，Alpha值保持不变
        ColorModel cm = ColorModel.getRGBdefault();
        for (int i = 1; i < ih - 1; i++) {
            for (int j = 1; j < iw - 1; j++) {
                int red, green, blue;
                int alpha = cm.getAlpha(pixels[i * iw + j]);
                // int red2 = cm.getRed(pixels[(i - 1) * iw + j]);
                int red4 = cm.getRed(pixels[i * iw + j - 1]);
                int red5 = cm.getRed(pixels[i * iw + j]);
                int red6 = cm.getRed(pixels[i * iw + j + 1]);
                // int red8 = cm.getRed(pixels[(i + 1) * iw + j]);
                // 水平方向进行中值滤波
                if (red4 >= red5) {
                    if (red5 >= red6) {
                        red = red5;
                    } else {
                        if (red4 >= red6) {
                            red = red6;
                        } else {
                            red = red4;
                        }
                    }
                } else {
                    if (red4 > red6) {
                        red = red4;
                    } else {
                        if (red5 > red6) {
                            red = red6;
                        } else {
                            red = red5;
                        }
                    }
                }
                int green4 = cm.getGreen(pixels[i * iw + j - 1]);
                int green5 = cm.getGreen(pixels[i * iw + j]);
                int green6 = cm.getGreen(pixels[i * iw + j + 1]);
                // 水平方向进行中值滤波
                if (green4 >= green5) {
                    if (green5 >= green6) {
                        green = green5;
                    } else {
                        if (green4 >= green6) {
                            green = green6;
                        } else {
                            green = green4;
                        }
                    }
                } else {
                    if (green4 > green6) {
                        green = green4;
                    } else {
                        if (green5 > green6) {
                            green = green6;
                        } else {
                            green = green5;
                        }
                    }
                }
                // int blue2 = cm.getBlue(pixels[(i - 1) * iw + j]);
                int blue4 = cm.getBlue(pixels[i * iw + j - 1]);
                int blue5 = cm.getBlue(pixels[i * iw + j]);
                int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
			    // int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);
			    // 水平方向进行中值滤波	
                if (blue4 >= blue5) {
                    if (blue5 >= blue6) {
                        blue = blue5;
                    } else {
                        if (blue4 >= blue6) {
                            blue = blue6;
                        } else {
                            blue = blue4;
                        }
                    }
                } else {
                    if (blue4 > blue6) {
                        blue = blue4;
                    } else {
                        if (blue5 > blue6) {
                            blue = blue6;
                        } else {
                            blue = blue5;
                        }
                    }
                }
                pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8
                        | blue;
            }
        }
        // 将数组中的象素产生一个图像
        Image tempImg = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(iw, ih, pixels, 0, iw));
        image = new BufferedImage(tempImg.getWidth(null),
                tempImg.getHeight(null), BufferedImage.TYPE_INT_BGR);
        image.createGraphics().drawImage(tempImg, 0, 0, null);
        return image;
    }


    public BufferedImage getGrey() {
        ColorConvertOp ccp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return image = ccp.filter(image, null);
    }


    // Brighten using a linear formula that increases all color values
    public BufferedImage getBrighten() {
        RescaleOp rop = new RescaleOp(1.25f, 0, null);
        return image = rop.filter(image, null);
    }


    // Blur by "convolving" the image with a matrix
    public BufferedImage getBlur() {
        float[] data = {.1111f, .1111f, .1111f, .1111f, .1111f, .1111f,.1111f, .1111f, .1111f,};
        ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data));
        return image = cop.filter(image, null);


    }


    // Sharpen by using a different matrix
    public BufferedImage getSharpen() {
        float[] data = {0.0f, -0.75f, 0.0f, -0.75f, 4.0f, -0.75f, 0.0f,-0.75f, 0.0f};
        ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data));
        return image = cop.filter(image, null);
    }


    // 11) Rotate the image 180 degrees about its center point
    public BufferedImage getRotate() {
        AffineTransformOp atop = new AffineTransformOp(
                AffineTransform.getRotateInstance(Math.PI,image.getWidth() / 2, image.getHeight() / 2),
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return image = atop.filter(image, null);
    }


    public BufferedImage getProcessedImg() {
        return image;
    }
    
    public static BufferedImage TouMingPic(int width, int height, int issave,String path,String type) {
    	// 创建一个可以保存的image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width,height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setStroke(new BasicStroke(1));
        // 释放对象
        g2d.dispose();
        // 保存文件
        if (issave == 1) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
                String dxx = df.format(new Date());
                // 另存为图片 即修改后图片
                String imgPathOut = path + dxx + "." + type;
                ImageIO.write(image, type, new File(imgPathOut));
               // System.out.println("透明ok:" + imgPathOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
    
	//将图片白色背景透明化处理，支持img,png,tif格式转png,tif格式输出，不支持img输出
	public static File imageConvert(File imageFile, String type) {
		try{
			BufferedImage bi = ImageIO.read(imageFile);
			ImageConvert flt = new ImageConvert(bi);
			flt.changeGrey();
			flt.getGrey();
			flt.getBrighten();
			bi = flt.getProcessedImg();
			String newPath = imageFile.getParent() + File.separator + "transImage";
			File path = new File(newPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			//先临时保存为纯白色背景图片
			String fileName = imageFile.getName().substring(0, imageFile.getName().lastIndexOf("."));
			String pngName = fileName + "-temp." + type;
			//String pngName = fileName + "-temp.png";
			String newFileName = newPath + File.separator + pngName;
			File file = new File(newFileName);
			ImageIO.write(bi, type, file);
			//ImageIO.write(bi, "png", file);
	
			BufferedImage image = ImageIO.read(new FileInputStream(file));
			// new一张一样大的图，用于存放修改后的图
			int width = image.getWidth();// 宽
			int height = image.getHeight();// 高
			// 创建一个image容器，用于存放处理后的image
			BufferedImage images = ImageConvert.TouMingPic(width, height, 0, null, type);
			Graphics2D g = images.createGraphics();
			// 将白色透明处理
			ImageFilter imgf = new ImageConvertFilter(255);
			FilteredImageSource fis = new FilteredImageSource(image.getSource(), imgf);
			Image im = Toolkit.getDefaultToolkit().createImage(fis);
			g.drawImage(im, 0, 0, width, height, null);
			g.dispose();
			// 透明图片将白色背景图片保存为透明 背景
			String outFileName = newPath + File.separator + fileName + "." + type;
			File outFile = new File(outFileName);
			ImageIO.write(images, type, outFile);
			//删除白色背景图片
			if(file.exists()){
				file.delete();
			}
			return outFile;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(imageFile.getName()+"将图片背景色透明化处理失败！！");
		}
		return null;
	}
	
}

class ImageConvertFilter extends RGBImageFilter {
    int alpha = 0;
    public ImageConvertFilter(int alpha) {
    // 用来接收需要过滤图象的尺寸，以及透明度
        this.canFilterIndexColorModel = true;
        this.alpha = alpha;
    }
    public int filterRGB(int x, int y, int rgb) {
        DirectColorModel dcm = (DirectColorModel) ColorModel.getRGBdefault();
        int red = dcm.getRed(rgb);
        int green = dcm.getGreen(rgb);
        int blue = dcm.getBlue(rgb);
        int alp = dcm.getAlpha(rgb);
        // 指定颜色替换为透明
        if (red == 255 && blue == 255 && green == 255) {
        	// 如果像素为白色，则让它透明
            alpha = 0;
        } else {
            alpha = 255;
        }
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
    

}
