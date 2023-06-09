package net.optifine.player;

import java.awt.Graphics;
import net.minecraft.client.renderer.IImageBuffer;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import kklient.cosmetics.CapeController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

public class CapeUtils
{
    private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");

    public static void downloadCape(final AbstractClientPlayer player)
    { 

String username = player.getNameClear();

        if (username != null && !username.isEmpty())
        {
          if(CapeController.<capeboolean> == "") {
        	  final ResourceLocation <capeboolean> = new ResourceLocation("<the location of the folder you made>(in my case)kklient/capes/fire.png");
        	  if(<capeboolean>!=null) {
        		  player.setLocationOfCape(<capeboolean>);
        	  }
          }
         }
          else {
        	  String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
              String mptHash = FilenameUtils.getBaseName(ofCapeUrl);
              final ResourceLocation rl = new ResourceLocation("capeof/" + mptHash);
              TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
              ITextureObject tex = textureManager.getTexture(rl);

              if (tex != null && tex instanceof ThreadDownloadImageData)
              {
                  ThreadDownloadImageData thePlayer = (ThreadDownloadImageData)tex;

                  if (thePlayer.imageFound != null)
                  {
                      if (thePlayer.imageFound.booleanValue())
                      {
                          player.setLocationOfCape(rl);
                      }

                      return;
                  }
              }

              IImageBuffer iib = new IImageBuffer()
              {
                  ImageBufferDownload ibd = new ImageBufferDownload();
                  public BufferedImage parseUserSkin(BufferedImage var1)
                  {
                      return CapeUtils.parseCape(var1);
                  }
                  public void func_152634_a()
                  {
                      player.setLocationOfCape(rl);
                  }
				@Override
				public void skinAvailable() {
				//nothing happens
				}
              };
              ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File)null, ofCapeUrl, (ResourceLocation)null, iib);
              textureCape.pipeline = true;
              textureManager.loadTexture(rl, textureCape);
          }
        }


    public static BufferedImage parseCape(BufferedImage img)
    {
        int i = 64;
        int j = 32;
        int k = img.getWidth();

        for (int l = img.getHeight(); i < k || j < l; j *= 2)
        {
            i *= 2;
        }

        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(img, 0, 0, (ImageObserver)null);
        graphics.dispose();
        return bufferedimage;
    }

    public static boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed)
    {
        return imageRaw.getWidth() > imageFixed.getHeight();
    }

    public static void reloadCape(AbstractClientPlayer player)
    {
        String s = player.getNameClear();
        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

        if (itextureobject instanceof SimpleTexture)
        {
            SimpleTexture simpletexture = (SimpleTexture)itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }

        player.setLocationOfCape((ResourceLocation)null);
        player.setElytraOfCape(false);
        downloadCape(player);
    }
}
