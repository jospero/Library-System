package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
			if(viewName.equals("LoginView")){
				return new LoginView(model);
			} else if(viewName.equals("MainView")){
				return new MainView(model);
			} else if(viewName.equals("AddBookView")) {
				return new AddBookView(model);
			} else if(viewName.equals("ModifyBookView")) {
				return new ModifyBookView(model);
			} else if(viewName.equals("AddWorkerView")) {
				return new AddWorkerView(model);
			} else if(viewName.equals("AddStudentBorrowerView")) {
				return new AddStudentBorrowerView(model);
			} else if(viewName.equals("WelcomeView")){
				return new WelcomeView(model);
			}

			return null;
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}
