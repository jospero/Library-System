// tabs=4
//************************************************************
//	COPYRIGHT 2009/2015 Sandeep Mitra and Michael Steves, The
//    College at Brockport, State University of New York. - 
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot 
// be reproduced, copied, or used in any shape or form without 
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports
import java.io.File;
import java.util.Properties;
import java.util.Vector;
import java.util.EventObject;

import common.PropertyFile;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import java.util.*;
// project imports
import common.StringList;
import impresario.IView;
import impresario.IModel;
import impresario.IControl;
import impresario.ControlRegistry;
import javafx.util.Duration;

//==============================================================
public abstract class View extends Group
	implements IView, IControl
{
	// private data
	protected IModel myModel;
	protected ControlRegistry myRegistry;

	// GUI components
	protected final ScaleTransition stSmall = new ScaleTransition();
	protected FadeTransition ft = new FadeTransition();
	// Class constructor
	//----------------------------------------------------------
	public View(IModel model, String classname)
	{
		myModel = model;
		String css = this.getClass().getResource("/resources/css/common.css").toExternalForm();
		this.getStylesheets().add(css);

		myRegistry = new ControlRegistry(classname);


		stSmall.setNode(this);
		stSmall.setFromX(0.0);
		stSmall.setFromY(0.0);
		stSmall.setToX(1.0);
		stSmall.setToY(1.0);
		stSmall.setDuration(new Duration(500));


		ft.setNode(this);
		ft.setDuration(new Duration(2000));
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);
	}
	
	
	//----------------------------------------------------------
	public void setRegistry(ControlRegistry registry)
	{
		myRegistry = registry;
	}
	
	// Allow models to register for state updates
	//----------------------------------------------------------
	public void subscribe(String key,  IModel subscriber)
	{
		myRegistry.subscribe(key, subscriber);
	}
		
		
	// Allow models to unregister for state updates
	//----------------------------------------------------------
	public void unSubscribe(String key, IModel subscriber)
	{
		myRegistry.unSubscribe(key, subscriber);
	}
	
   	
}

