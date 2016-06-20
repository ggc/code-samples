scd $HOME/git_repos/codeHere/loc8r/
(echo $GIO_LAUNCHED_DESKTOP_FILE | grep "guake") >> /dev/null
if [ $? -eq 0 ];
#Executed on guake
then
    guake -n . ; sleep .5;
    guake -n . ; sleep .5;
    gnome-open $HOME/Data/DOCS/Getting_MEAN_with_Mongo_Express_Angular_and_Nodejs.pdf; sleep .5;
    atom .; sleep .5;
    #TODO Ask to open nodemon
#Executed on terminal
else
    #TODO
    echo "Open Guake\n";
fi
