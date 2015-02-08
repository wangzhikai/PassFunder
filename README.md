# NiShang PassFunder
Continental RockNRollers
-----Github tricks (may be trivial or in-complete)
git config --global http.sslVerify false
git clone https://github.com/wangzhikai/PassFunder.git
cd PassFunder/
git remote set-url origin git@github.com:wangzhikai/PassFunder.git

#ssh-add -l shows nothing, need the following steps:
ssh-keygen
eval "$(ssh-agent -s)"
ssh-add
cat ~/.ssh/id_rsa.pub # Login to github,click 'username' at  top right, 'edit profile', 'SSH keys', 'Add SSH key', add the cat result
