package fr.treeptik.cloudunit.cli.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import fr.treeptik.cloudunit.cli.utils.ServerUtils;
import fr.treeptik.cloudunit.cli.utils.VolumeService;
import fr.treeptik.cloudunit.model.Volume;

@Component
public class VolumeCommands implements CommandMarker {
	@Autowired
	private VolumeService volumeService;

	@Autowired
	private ServerUtils serverUtils;
	
	@Autowired
	private CliFormatter formatter;

	@CliCommand(value = "create-volume", help = "Create a new volume")
	public String createVolume(
			@CliOption(key = { "name" }, mandatory = true, help = "Name of the local volume") String name) {
		volumeService.createVolume(name);
		return formatter.unlessQuiet("Volume added");
	}

	@CliCommand(value = "rm-volume", help = "Remove a volume")
	public String removeVolume(
			@CliOption(key = {"name"}, mandatory = true, help = "Name of the deleted volume") String name) {
		volumeService.removeVolume(name);
		return formatter.unlessQuiet("Volume removed");
	}

	@CliCommand(value = "mount-volume", help = "Mount the volume into the container")
	public String mountVolume(
			@CliOption(key = {"volume-name"}, mandatory = true, help = "Name of the volume") String name,
			@CliOption(key = {"path"}, mandatory = true, help = "Path in the container") String path,
			@CliOption(key = {"read-only"}, unspecifiedDefaultValue = "false", mandatory = false, help = "Mode read-only") boolean mode,
			@CliOption(key = {"container-name"}, mandatory = false, help = "Container for the volume") String containerName,
			@CliOption(key = {"application-name"}, mandatory = false, help = "Application for the volume") String applicationName) {
		serverUtils.mountVolume(name, path, mode, containerName, applicationName);
		return formatter.unlessQuiet("Volume mounted");
	}

	@CliCommand(value = "unmount-volume", help = "Unmount the volume from the container")
	public String mountVolume(
			@CliOption(key = {"volume-name"}, mandatory = true, help = "Name of the volume") String name,
			@CliOption(key = {"container-name"}, mandatory = true, help = "Container for the volume") String containerName) {
		serverUtils.unmountVolume(name, containerName);
		return formatter.unlessQuiet("Volume unmounted");
	}

	@CliCommand(value = "list-volumes", help = "Display all volumes")
	public String displayVolumes() {
		List<Volume> volumes = volumeService.displayVolumes();
		List<String> volumeNames = volumes.stream()
		        .map(v -> v.getName())
		        .collect(Collectors.toList());
		return formatter.list(volumeNames);
	}
}
