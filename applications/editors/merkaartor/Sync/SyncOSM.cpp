#include "Sync/SyncOSM.h"

#include "MainWindow.h"
#include "Command/Command.h"
#include "Map/MapDocument.h"
#include "Map/DownloadOSM.h"
#include "Sync/DirtyList.h"

#include <QtGui/QMessageBox>

void syncOSM(MainWindow* theMain, const QString& aWeb, const QString& aUser, const QString& aPwd, bool Use4Api)
{
	if (checkForConflicts(theMain->document()))
	{
		QMessageBox::warning(theMain,MainWindow::tr("Unresolved conflicts"), MainWindow::tr("Please resolve existing conflicts first"));
		return;
	}

	DirtyListBuild Future;
	theMain->document()->history().buildDirtyList(Future);
	DirtyListDescriber Describer(theMain->document(),Future);
	if (Describer.showChanges(theMain))
	{
		Future.resetUpdates();
		DirtyListExecutor Exec(theMain->document(),Future,aWeb,aUser,aPwd,Describer.tasks(), Use4Api);
		Exec.executeChanges(theMain);
	}
}

