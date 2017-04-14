package userinterface;

import impresario.IModel;
import userinterface.book.AddBookView;
import userinterface.book.BookCollectionView;
import userinterface.book.ModifyBookView;
import userinterface.book.CheckOutBookView;
import userinterface.book.SearchBookView;
import userinterface.studentborrower.AddStudentBorrowerView;
import userinterface.studentborrower.ModifyStudentBorrowerView;
import userinterface.studentborrower.SearchStudentBorrowerView;
import userinterface.studentborrower.StudentBorrowerCollectionView;
import userinterface.worker.AddWorkerView;
import userinterface.worker.ModifyWorkerView;
import userinterface.worker.SearchWorkerView;
import userinterface.worker.WorkerCollectionView;

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
			} else if(viewName.equals("CheckOutBookView")) {
				return new CheckOutBookView(model);
			} else if(viewName.equals("ModifyWorkerView")) {
				return new ModifyWorkerView(model);
			}  else if(viewName.equals("ModifyStudentBorrowerView")) {
				return new ModifyStudentBorrowerView(model);
			} else if(viewName.equals("BookCollectionView")) {
				return new BookCollectionView(model);
			} else if(viewName.equals("AddWorkerView")) {
				return new AddWorkerView(model);
			} else if(viewName.equals("AddStudentBorrowerView")) {
				return new AddStudentBorrowerView(model);
			} else if(viewName.equals("WelcomeView")){
				return new WelcomeView(model);
			}  else if(viewName.equals("SearchBookView")){
				return new SearchBookView(model);
			} else if(viewName.equals("SearchWorkerView")){
				return new SearchWorkerView(model);
			} else if(viewName.equals("SearchStudentBorrowerView")){
				return new SearchStudentBorrowerView(model);
			} else if(viewName.equals("WorkerCollectionView")) {
				return new WorkerCollectionView(model);
			} else if(viewName.equals("StudentBorrowerCollectionView")) {
				return new StudentBorrowerCollectionView(model);
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
