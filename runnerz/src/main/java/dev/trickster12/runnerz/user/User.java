package dev.trickster12.runnerz.user;

public class User {

    /*
    * {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  },
    * */

        private Integer id;
        private String name;
        private String username;
        private String email;
        private Address address;
        private String phone;
        private String website;
        private Company company;

        public User() {
        }

        public User(Integer id, String name, String username, String email, Address address, String phone, String website, Company company) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.website = website;
            this.company = company;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public Address getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        public String getWebsite() {
            return website;
        }

        public Company getCompany() {
            return company;
        }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company=" + company +
                '}';
    }
}
