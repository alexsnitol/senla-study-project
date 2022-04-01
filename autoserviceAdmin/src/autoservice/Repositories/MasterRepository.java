package autoservice.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MasterRepository {

    protected ArrayList<Master> masters = new ArrayList<>();
    private int lastId = -1;

    public List<Master> getMasters() {
        return masters;
    }

    public void setMasters(List<Master> masters) {
        this.masters = (ArrayList<Master>) masters;
    }

    public void addMaster(Master master) {
        lastId++;
        master.setId(this.lastId);
        masters.add(master);
    }

    public void deleteMaster(int index) {
        if (index >= 0 && index < masters.size())
            masters.remove(index);
    }

    public void deleteMasterById(int id) {
        Master master = this.getMasterById(id);

        if (master != null)
            this.masters.remove(master);
    }

    public Master getMaster(int index) {
        if (index >= 0 && index < masters.size())
            return masters.get(index);
        return null;
    }

    public Master getMasterById(int id) {
        for (Master master : this.masters) {
            if (master.getId() == id)
                return master;
        }

        return null;
    }

    public int sizeMasters() {
        return masters.size();
    }

    public String getInfoOfMasters() {
        String result = "";

        for (int i = 0; i < masters.size(); i++) {
            result += "" + (i + 1) + ". " + masters.get(i).toString();
            if (i != masters.size() - 1)
                result += "\n";
        }

        return result;
    }


    public MasterRepository getMastersSortedByAlphabetically() {
        MasterRepository masterRepository = new MasterRepository();
        List<Master> masters = (ArrayList<Master>) this.masters.clone();

        masters = this.masters.stream()
                .sorted((m1, m2) -> (m1.getLastName() + m1.getFirstName() + m1.getPatronymic()).compareTo(m2.getLastName() + m2.getFirstName() + m2.getPatronymic()))
                .collect(Collectors.toList());

        masterRepository.setMasters(masters);

        return masterRepository;
    }

    public MasterRepository getMastersSortedByNumberOfActiveOrders() {
        MasterRepository masterRepository = new MasterRepository();
        List<Master> masters = (ArrayList<Master>) this.masters.clone();

        masters = this.masters.stream()
                .sorted(Comparator.comparingInt(Master::getNumberOfActiveOrders))
                .collect(Collectors.toList());

        masterRepository.setMasters(masters);

        return masterRepository;
    }
}