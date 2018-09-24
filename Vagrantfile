# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.
    
  required_plugins = %w( vagrant-disksize )
  _retry = false
  required_plugins.each do |plugin|
    unless Vagrant.has_plugin? plugin
      system "vagrant plugin install #{plugin}"
      _retry=true
    end
  end
    
  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "ubuntu/xenial64"

  config.disksize.size = '50GB'
  
  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  # config.vm.network "forwarded_port", guest: 80, host: 8080

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80,   host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    # vb.gui = true
  
    # Customize the amount of memory on the VM:
    vb.memory = "8192"
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
      apt-get -qq update
      # downgrade to java 7
      add-apt-repository ppa:openjdk-r/ppa
      apt-get update
      apt-get install --yes openjdk-7-jdk
      # Crawl2 build (gradle)
      PREVIOUS_DIR=`pwd`
      cd /vagrant
      ###  uncomment this block to get gradle to use local nexus repos ########
      # 
      # key for local nexus repo
      # openssl s_client -connect developer.genedb.org:443 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/developer.genedb.org
      # mkdir -p /Library/Java/Home/lib/security/cacerts
      # keytool -import -alias developer.genedb.org -trustcacerts -noprompt -storepass changeit -keystore /Library/Java/Home/lib/security/cacerts -file /tmp/developer.genedb.org
      # 
      ###  uncomment this block to prevent gradle using local nexus repos #####
      # 
      # comment out nexus repo URLs in gradle build file
      mv build.gradle build.gradle.old
      perl -0777 -pe 's~([^\n]*maven\s*\{.*\n+)([^\n]*url\s+"https://developer\.genedb\.org/nexus/.*"\s*\n+)([^\n]*}\s*\n+)~// \\1// \\2// \\3~g' build.gradle.old > build.gradle
      # 
      ###  end of alternative gradle/nexus blocks #############################
      ./gradlew build --debug
      cd $PREVIOUS_DIR
      # for convenience, link from vagrant home dir to host machine git repo
      ln -s /vagrant /home/vagrant/Crawl2
      # done
      echo '*** PROVISIONING COMPLETE ***'
  SHELL
end
